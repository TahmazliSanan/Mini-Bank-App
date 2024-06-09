package org.pronet.app.services.implementations;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.pronet.app.entities.Transaction;
import org.pronet.app.entities.User;
import org.pronet.app.payloads.EmailDetails;
import org.pronet.app.repositories.TransactionRepository;
import org.pronet.app.repositories.UserRepository;
import org.pronet.app.services.EmailService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BankStatement {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;
    private static final String FILE = "C:\\Users\\user\\Documents\\FlexBank - My Statement.pdf";

    public BankStatement(UserRepository userRepository, TransactionRepository transactionRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.emailService = emailService;
    }

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate)
            throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository
                .findAll()
                .stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start))
                .filter(transaction -> transaction.getCreatedAt().isEqual(end))
                .toList();

        User user = userRepository.findByAccountNumber(accountNumber);
        String customerFullName = user.getFirstName() + " " + user.getLastName();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("FlexBank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.CYAN);
        bankName.setPadding(20f);
        bankName.setPaddingLeft(180f);
        PdfPCell bankAddress = new PdfPCell(new Phrase("Address: Baku, Azerbaijan"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell sd = new PdfPCell(new Phrase("Start Date: " + startDate));
        sd.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("Statement Of Account"));
        statement.setBorder(0);
        PdfPCell ed = new PdfPCell(new Phrase("End Date: " + endDate));
        ed.setBorder(0);
        PdfPCell cfn = new PdfPCell(new Phrase("Full Name: " + customerFullName));
        cfn.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell accNumber = new PdfPCell(new Phrase("Account Number: " + user.getAccountNumber()));
        accNumber.setBorder(0);
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Address: " + user.getAddress()));
        address.setBorder(0);

        statementInfo.addCell(sd);
        statementInfo.addCell(statement);
        statementInfo.addCell(ed);
        statementInfo.addCell(cfn);
        statementInfo.addCell(space);
        statementInfo.addCell(accNumber);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("Date"));
        date.setBorder(0);
        date.setBackgroundColor(BaseColor.CYAN);
        PdfPCell transactionType = new PdfPCell(new Phrase("Type"));
        transactionType.setBorder(0);
        transactionType.setBackgroundColor(BaseColor.CYAN);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("Amount"));
        transactionAmount.setBorder(0);
        transactionAmount.setBackgroundColor(BaseColor.CYAN);
        PdfPCell transactionStatus = new PdfPCell(new Phrase("Status"));
        transactionStatus.setBorder(0);
        transactionStatus.setBackgroundColor(BaseColor.CYAN);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(transactionStatus);

        transactionList.forEach(
                transaction -> {
                    transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
                    transactionTable.addCell(new Phrase(transaction.getType()));
                    transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
                    transactionTable.addCell(new Phrase(transaction.getStatus()));
                }
        );

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails = EmailDetails
                .builder()
                .recipient(user.getEmail())
                .subject("Statement Of Account")
                .textBody("This file (as PDF) is your statement of account!")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;
    }
}
