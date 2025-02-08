package com.basic.bank.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private static final String BANK_LOGO_PATH = "src/main/resources/static/images/img.png";
    private static final String BANK_NAME = "SARTHAK'S Bank Pvt. Ltd.";

    public byte[] generateLoanTransactionReceipt(String accountNumber, String loanRequestId, String transactionType,
                                                 BigDecimal transactionAmount, BigDecimal remainingLoanBalance) {
        return generateReceipt(accountNumber, loanRequestId, transactionType, transactionAmount, remainingLoanBalance, "Loan Transaction Receipt");
    }

    public byte[] generateLoanRepaymentReceipt(String accountNumber, String loanRequestId, BigDecimal repaymentAmount, BigDecimal remainingBalance) {
        return generateReceipt(accountNumber, loanRequestId, "Loan Repayment", repaymentAmount, remainingBalance, "Loan Repayment Receipt");
    }

    public byte[] generateLoanCompletionReceipt(String accountNumber, String loanRequestId) {
        return generateReceipt(accountNumber, loanRequestId, "Full Loan Repayment", BigDecimal.ZERO, BigDecimal.ZERO, "Loan Completion Receipt");
    }

    public byte[] generateTransactionDepositReceipt(String accountNumber, BigDecimal depositAmount) {
        return generateReceipt(accountNumber, "N/A", "Deposit", depositAmount, BigDecimal.ZERO, "Deposit Transaction Receipt");
    }

    public byte[] generateTransactionWithdrawReceipt(String accountNumber, BigDecimal withdrawAmount) {
        return generateReceipt(accountNumber, "N/A", "Withdraw", withdrawAmount, BigDecimal.ZERO, "Withdrawal Transaction Receipt");
    }

    public byte[] generateTransactionTransferReceipt(String accountNumber, String destinationAccount, BigDecimal transferAmount) {
        return generateReceipt(accountNumber, destinationAccount, "Transfer", transferAmount, BigDecimal.ZERO, "Transfer Transaction Receipt");
    }

    private byte[] generateReceipt(String accountNumber, String referenceId, String transactionType, BigDecimal transactionAmount, BigDecimal remainingBalance, String title) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(byteArrayOutputStream);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument, PageSize.A4)) {

            document.setMargins(30, 30, 30, 30);
            addBankLogo(document);
            addTitle(document, title);
            addGeneratedDate(document);
            addTransactionDetails(document, accountNumber, referenceId, transactionType, transactionAmount, remainingBalance);
            addFooter(document);

            document.close();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating the PDF receipt: " + e.getMessage());
        }
    }

    private void addBankLogo(Document document) throws IOException {
        File logoFile = new File(BANK_LOGO_PATH);
        if (logoFile.exists()) {
            ImageData logoData = ImageDataFactory.create(BANK_LOGO_PATH);
            Image logo = new Image(logoData).setWidth(80).setHorizontalAlignment(HorizontalAlignment.LEFT);
            document.add(logo);
        } else {
            document.add(new Paragraph(BANK_NAME)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.LEFT));
        }
    }

    private void addTitle(Document document, String title) {
        document.add(new Paragraph(title)
                .setFontSize(18)
                .setFontColor(ColorConstants.BLUE)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10));
    }

    private void addGeneratedDate(Document document) {
        document.add(new Paragraph("Generated on: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(10));
    }

    private void addTransactionDetails(Document document, String accountNumber, String referenceId, String transactionType,
                                       BigDecimal transactionAmount, BigDecimal remainingBalance) {
        document.add(new Paragraph("Transaction Details Below:")
                .setFontSize(12)
                .setMarginBottom(10));

        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 5}))
                .useAllAvailableWidth()
                .setMarginTop(10);

        table.addCell(createCell("Account Number:", true));
        table.addCell(createCell(accountNumber, false));

        table.addCell(createCell("Reference ID:", true));
        table.addCell(createCell(referenceId, false));

        table.addCell(createCell("Transaction Type:", true));
        table.addCell(createCell(transactionType, false));

        table.addCell(createCell("Transaction Amount:", true));
        table.addCell(createCell("INR " + transactionAmount, false));

        if (remainingBalance.compareTo(BigDecimal.ZERO) > 0) {
            table.addCell(createCell("Remaining Balance:", true));
            table.addCell(createCell("INR " + remainingBalance, false));
        }

        document.add(table);
    }

    private void addFooter(Document document) {
        document.add(new Paragraph("Thank you for banking with XYZ Bank!")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20));
    }

    private Cell createCell(String content, boolean bold) {
        Cell cell = new Cell().add(new Paragraph(content).setFontSize(11));
        cell.setBorder(Border.NO_BORDER);
        cell.setPadding(5);
        if (bold) {
            cell.simulateBold();
        }
        return cell;
    }
}

