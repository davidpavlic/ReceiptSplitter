package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.enums.EnvConstants;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.exception.HttpResponseException;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The ImageReceiptExtractor class is responsible for extracting receipt data from an image file.
 * The extracted data includes the total price and a list of items.
 * Each item includes the quantity, name, and total price.
 * <p>
 * The class includes nested classes for representing the extracted receipt and its items, and an exception class for handling errors.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public class ImageReceiptExtractor {
    private static final Logger logger = Logger.getLogger(ImageReceiptExtractor.class.getName());
    private static final String MODEL_ID = "prebuilt-receipt";

    private final String endpoint;
    private final String key;

    /**
     * Constructs a new ImageReceiptExtractor instance.
     * <p>
     * The constructor retrieves the Azure AI Form Recognizer endpoint and key from the system properties.
     * These properties are used to authenticate the client with the Azure AI Form Recognizer service.
     */
    public ImageReceiptExtractor() {
        endpoint = System.getProperty(EnvConstants.AZURE_AI_FORM_RECOGNIZER_ENDPOINT.getKey());
        key = System.getProperty(EnvConstants.AZURE_AI_FORM_RECOGNIZER_KEY.getKey());
    }

    /**
     * Extracts the receipt data from an image file.
     * The image file must contain a receipt.
     *
     * @param file The image file to extract the receipt data from.
     * @return The extracted OCR as a ReceiptORC object.
     * @throws ImageReceiptExtractorException If an error occurs during the extraction process.
     * @throws NullPointerException           If the file is null.
     */
    public ReceiptOCR extractReceiptOCR(File file) throws ImageReceiptExtractorException {
        Objects.requireNonNull(file, "File must not be null");

        try {
            DocumentAnalysisClient client = getDocumentAnalysisClient();

            BinaryData binaryData = BinaryData.fromFile(file.toPath());
            SyncPoller<OperationResult, AnalyzeResult> analyzeLayoutResultPoller = client.beginAnalyzeDocument(MODEL_ID, binaryData);
            Map<String, DocumentField> analyzedReceiptFields = getAnalyzedReceiptFields(analyzeLayoutResultPoller);

            List<ReceiptItemOCR> receiptItemOCRList = extractReceiptItemOCRList(analyzedReceiptFields);

            return new ReceiptOCR(roundExtractedPrice(analyzedReceiptFields.get("Total").getValueAsDouble()), receiptItemOCRList);
        } catch (HttpResponseException e) {
            logger.severe("Network error occurred: " + e.getMessage());
            throw new ImageReceiptExtractorException("Could not extract OCR from image. OCR service not available.", e);
        } catch (Exception e) {
            logger.severe("Error while extracting OCR from image: " + e.getMessage());
            throw new ImageReceiptExtractorException("Could not extract OCR from image.", e);
        }
    }

    /**
     * Represents a receipt extracted from an image with OCR.
     *
     * @param totalPrice         The total price of the receipt.
     * @param receiptItemOCRList The items on the receipt.
     */
    public record ReceiptOCR(double totalPrice, List<ReceiptItemOCR> receiptItemOCRList) {

        /**
         * Creates a new ReceiptOCR instance.
         *
         * @throws NullPointerException if the items are null.
         */
        public ReceiptOCR {
            Objects.requireNonNull(receiptItemOCRList, "Receipt Items must not be null");
        }
    }

    /**
     * Represents a single item on a receipt extracted from an image with OCR.
     *
     * @param amount The amount of the specific item on the receipt.
     * @param name   The name of the item.
     * @param price  The total price of the item.
     */
    public record ReceiptItemOCR(int amount, String name, double price) {

        /**
         * Creates a new ReceiptItemOCR instance.
         *
         * @throws NullPointerException if the name is null.
         */
        public ReceiptItemOCR {
            Objects.requireNonNull(name, "Name must not be null");
        }
    }

    /**
     * Checked Exception thrown when an error occurs during the image extraction process.
     */
    public static class ImageReceiptExtractorException extends Exception {
        public ImageReceiptExtractorException(String message) {
            super(message);
        }

        public ImageReceiptExtractorException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Returns a DocumentAnalysisClient instance.
     * <p>
     * The DocumentAnalysisClient is used to analyze documents using the Azure AI Form Recognizer service.
     * The client is authenticated using the AzureKeyCredential created with the key retrieved from the system properties.
     *
     * @return a DocumentAnalysisClient instance
     */
    @NotNull
    private DocumentAnalysisClient getDocumentAnalysisClient() {
        return new DocumentAnalysisClientBuilder().credential(new AzureKeyCredential(key)).endpoint(endpoint).buildClient();
    }

    /**
     * Extracts a list of ReceiptItemOCR instances from the analyzed receipt fields.
     * <p>
     * The method filters out null items and maps each item to a ReceiptItemOCR instance.
     * The ReceiptItemOCR instance is created with the item quantity, description, and total price.
     * If the total price is not available, it is calculated using the item quantity and unit price.
     *
     * @param analyzedReceiptFields the analyzed receipt fields
     * @return a list of ReceiptItemOCR instances
     */
    @NotNull
    private List<ReceiptItemOCR> extractReceiptItemOCRList(Map<String, DocumentField> analyzedReceiptFields) {
        return analyzedReceiptFields.get("Items").getValueAsList().stream().filter(Objects::nonNull).map(item -> {
            String itemDescription = getDocumentFieldContent(item, "Description", "");
            int itemQuantity = getDocumentFieldIntValue(item, "Quantity", 1);
            double totalPrice = calculateTotalPrice(item, itemQuantity);

            return new ReceiptItemOCR(itemQuantity, itemDescription, roundExtractedPrice(totalPrice));
        }).toList();
    }


    /**
     * Returns the analyzed receipt fields from the analyze layout result poller.
     * <p>
     * The method gets the final result from the poller and retrieves the first document as the analyzed receipt.
     * If the analyzed receipt does not contain the "Items" or "Total" fields, an ImageReceiptExtractorException is thrown.
     *
     * @param analyzeLayoutResultPoller the analyze layout result poller
     * @return the analyzed receipt fields
     * @throws ImageReceiptExtractorException if the analyzed receipt does not contain the "Items" or "Total" fields
     */
    @NotNull
    private Map<String, DocumentField> getAnalyzedReceiptFields(SyncPoller<OperationResult, AnalyzeResult> analyzeLayoutResultPoller) throws ImageReceiptExtractorException {
        AnalyzeResult analyzeResult = analyzeLayoutResultPoller.getFinalResult();

        // The image to analyze is a single receipt, so we can assume that the first document is the analyzed receipt
        if (analyzeResult.getDocuments().isEmpty()) {
            logger.warning("No document to extract found in the image");
            throw new ImageReceiptExtractorException("No document to extract found in the image");
        }

        AnalyzedDocument analyzedReceipt = analyzeResult.getDocuments().getFirst();
        Map<String, DocumentField> analyzedReceiptFields = analyzedReceipt.getFields();

        if (!analyzedReceiptFields.containsKey("Items") || !analyzedReceiptFields.containsKey("Total")) {
            logger.warning("No receipt items or total price found in the receipt");
            throw new ImageReceiptExtractorException("No receipt items or total price found in the receipt");
        }

        return analyzedReceiptFields;
    }

    private int getDocumentFieldIntValue(DocumentField receiptItem, String fieldName, int defaultValue) {
        DocumentField field = receiptItem.getValueAsMap().get(fieldName);
        if (field != null && field.getType() == DocumentFieldType.DOUBLE) {
            return field.getValueAsDouble().intValue();
        }

        return defaultValue;
    }

    private String getDocumentFieldContent(DocumentField receiptItem, String fieldName, String defaultValue) {
        DocumentField field = receiptItem.getValueAsMap().get(fieldName);
        return field != null ? field.getContent() : defaultValue;
    }

    private double calculateTotalPrice(DocumentField receiptItem, int itemQuantity) {
        DocumentField totalPriceField = receiptItem.getValueAsMap().get("TotalPrice"); // Total price for per item
        DocumentField priceField = receiptItem.getValueAsMap().get("Price"); // Extracted price per item unit, if available and extracted correctly

        if (totalPriceField == null && priceField == null) {
            return 0;
        }

        double unitPrice = getUnitPrice(priceField);

        // In case the total price is not present, but the price per unit is,
        //  then the extracted unit price is most likely the total price.
        // Reason for this is that most receipts either have both prices or only the total price.
        // If the total price is missing, then the OCR most likely misinterpreted the total price as the unit price.
        if (priceField != null && totalPriceField == null) {
            return unitPrice;
        }

        double calculatedTotalPrice = unitPrice * itemQuantity;  // Calculate total price using quantity
        double recordedTotalPrice = getUnitPrice(totalPriceField);  // Get recorded total price, should already be total

        if (priceField != null) {
            return Math.abs(recordedTotalPrice - calculatedTotalPrice) < 0.01  // Margin of error with double comparison from extracted values
                    ? recordedTotalPrice : chooseMostConfidentPrice(totalPriceField, priceField, calculatedTotalPrice);
        }

        // If only one of the fields is present, return the value of the present field
        return recordedTotalPrice > 0 ? recordedTotalPrice : calculatedTotalPrice;
    }

    private double chooseMostConfidentPrice(DocumentField totalPriceField, DocumentField priceField, double calculatedTotalPrice) {
        double recordedTotalPrice = getUnitPrice(totalPriceField);
        return totalPriceField.getConfidence() > priceField.getConfidence() ? recordedTotalPrice : calculatedTotalPrice;
    }

    private double getUnitPrice(DocumentField field) {
        if (field == null) return 0;

        return field.getValueAsDouble();
    }

    // Round the extracted price to two decimal places
    private static double roundExtractedPrice(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
