package com.example.gobol.tabcommunication.printer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import com.example.gobol.tabcommunication.activity.MainActivity;

import org.apache.pdfbox.PDFToImage;

import java.io.CharArrayWriter;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Ajinkya on 1/19/2016.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
    private PrintedPdfDocument mPdfDocument;
    private Context mainActivity;
    private int totalPages = 0;
    private CharArrayWriter writtenPagesArray = new CharArrayWriter();

    public MyPrintDocumentAdapter(Context mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
// Create a new PdfDocument with the requested page attributes
        mPdfDocument = new PrintedPdfDocument(mainActivity, newAttributes);

        // Respond to cancellation request
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        // Compute the expected number of printed pages
        //totalPages = computePageCount(newAttributes);
        totalPages = 3;

        //int pages = 1;

        if (totalPages > 0) {
            // Return print information to print framework
            PrintDocumentInfo.Builder info = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalPages);
            PrintDocumentInfo printDocumentInfo = info.build();
            // Content layout reflow is complete
            callback.onLayoutFinished(printDocumentInfo, true);
        } else {
            // Otherwise report an error to the print framework
            callback.onLayoutFailed("Page count calculation failed.");
        }
    }

    private int computePageCount(PrintAttributes printAttributes) {
        int itemsPerPage = 4; // default item count for portrait mode

        PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
        if (!pageSize.isPortrait()) {
            // Six items per page in landscape orientation
            itemsPerPage = 6;
        }

        // Determine number of print items
//        int printItemCount = getPrintItemCount();
        int printItemCount = 2;


        return (int) Math.ceil(printItemCount / itemsPerPage);
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        // Iterate over each page of the document,
        // check if it's in the output range.
        for (int i = 0; i < totalPages; i++) {
            // Check to see if this page is in the output range.
            if (containsPage(pageRanges, i)) {
                // If so, add it to writtenPagesArray. writtenPagesArray.size()
                // is used to compute the next output page index.

                writtenPagesArray.append("i" + i);
                PdfDocument.Page page = mPdfDocument.startPage(i);

                // check for cancellation
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    mPdfDocument.close();
                    mPdfDocument = null;
                    return;
                }

                // Draw page content for printing
                drawPage(page);

                // Rendering is complete, so page can be finalized.
                mPdfDocument.finishPage(page);
            }
        }

        // Write PDF document to file
        try {
            mPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            mPdfDocument.close();
            mPdfDocument = null;
        }

//        convertPdfToImage();
        PageRange[] writtenPages = computeWrittenPages(pageRanges);
        // Signal the print framework the document is complete
        callback.onWriteFinished(writtenPages);
    }

    private void convertPdfToImage() {
        String pdfPath = "sdcard/file.pdf";
//config option 1:convert all document to image
        String[] args_1 = new String[3];
        args_1[0] = "-outputPrefix";
        args_1[1] = "my_image_1";
        args_1[2] = pdfPath;

//config option 2:convert page 1 in pdf to image
        String[] args_2 = new String[7];
        args_2[0] = "-startPage";
        args_2[1] = "1";
        args_2[2] = "-endPage";
        args_2[3] = "1";
        args_2[4] = "-outputPrefix";
        args_2[5] = "my_image_2";
        args_2[6] = pdfPath;

        try {
// will output "my_image_1.jpg"
            PDFToImage.main(args_1);

// will output "my_image_2.jpg"
            PDFToImage.main(args_2);
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error(e.getMessage(), e);
        }
    }

    private boolean containsPage(PageRange[] pageRanges, int i) {

        return true;
    }

    private PageRange[] computeWrittenPages(PageRange[] mPdfDocument) {
        return mPdfDocument;
    }

    private PdfDocument.Page drawPage(PdfDocument.Page page) {
        Canvas canvas = page.getCanvas();

        // units are in points (1/72 of an inch)
        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(36);
        canvas.drawText("Test Title", leftMargin, titleBaseLine, paint);

        paint.setTextSize(11);
        canvas.drawText("Test paragraph", leftMargin, titleBaseLine + 25, paint);

        paint.setColor(Color.BLUE);
        canvas.drawRect(100, 100, 172, 172, paint);

        return page;
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}
