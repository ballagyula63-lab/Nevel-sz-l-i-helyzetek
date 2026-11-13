package com.example.print

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.print.pdf.PrintedPdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.res.ResourcesCompat
import com.example.R
import com.example.model.SjtReport
import java.io.FileOutputStream
import java.io.IOException

object SjtReportPrinter {

    fun printReport(context: Context, report: SjtReport) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return
        val jobName = "Neveloszulo_SJT_Ertekelolap_${report.participantName.replace(" ", "_")}"
        val adapter = SjtPdfPrintAdapter(context, report)
        val attributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()
        printManager.print(jobName, adapter, attributes)
    }
}

private class SjtPdfPrintAdapter(
    private val context: Context,
    private val report: SjtReport
) : PrintDocumentAdapter() {

    private var printAttributes: PrintAttributes? = null
    private var totalPages: Int = 1

    private val serifTypeface: Typeface by lazy {
        ResourcesCompat.getFont(context, R.font.lora) ?: Typeface.SERIF
    }

    private val sansTypeface: Typeface by lazy {
        ResourcesCompat.getFont(context, R.font.source_sans_3) ?: Typeface.SANS_SERIF
    }

    private val logoBitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.btesz_logo)
    }

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        if (cancellationSignal?.isCanceled == true) {
            callback.onLayoutCancelled()
            return
        }
        printAttributes = newAttributes
        val dummyPdf = PrintedPdfDocument(context, newAttributes)
        totalPages = renderDocument(dummyPdf, newAttributes, writePages = false)
        dummyPdf.close()

        val info = PrintDocumentInfo.Builder("neveloszulo_sjt_ertekelolap.pdf")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(totalPages)
            .build()
        callback.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pages: Array<out PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback
    ) {
        val attrs = printAttributes ?: return
        val pdfDocument = PrintedPdfDocument(context, attrs)
        try {
            renderDocument(pdfDocument, attrs, writePages = true)
            if (cancellationSignal?.isCanceled == true) {
                callback.onWriteCancelled()
                return
            }
            FileOutputStream(destination.fileDescriptor).use { out ->
                pdfDocument.writeTo(out)
            }
            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        } catch (e: IOException) {
            callback.onWriteFailed(e.message)
        } finally {
            pdfDocument.close()
        }
    }

    private fun renderDocument(
        pdfDocument: PrintedPdfDocument,
        attrs: PrintAttributes,
        writePages: Boolean
    ): Int {
        val pageWidth = ((attrs.mediaSize?.widthMils ?: 8268) * 72f / 1000f).toInt().coerceAtLeast(595)
        val pageHeight = ((attrs.mediaSize?.heightMils ?: 11693) * 72f / 1000f).toInt().coerceAtLeast(842)
        val margin = 40f
        val contentWidth = (pageWidth - margin * 2).toInt()
        val bottomLimit = pageHeight - margin

        val inkColor = Color.parseColor("#192E2A")
        val greenColor = Color.parseColor("#205648")
        val mutedColor = Color.parseColor("#425B53")
        val lineColor = Color.parseColor("#CAD9D2")
        val noticeBgColor = Color.parseColor("#FFF3DA")
        val noticeBorderColor = Color.parseColor("#9D661E")
        val noticeTextColor = Color.parseColor("#533915")

        val h2Paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = Typeface.create(serifTypeface, Typeface.BOLD)
            textSize = 18f
            color = inkColor
        }
        val h3Paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = Typeface.create(sansTypeface, Typeface.BOLD)
            textSize = 12f
            color = inkColor
        }
        val scorePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = Typeface.create(serifTypeface, Typeface.BOLD)
            textSize = 24f
            color = greenColor
        }
        val bodyPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = sansTypeface
            textSize = 10f
            color = inkColor
        }
        val boldBodyPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = Typeface.create(sansTypeface, Typeface.BOLD)
            textSize = 10f
            color = inkColor
        }
        val mutedPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = sansTypeface
            textSize = 9.5f
            color = mutedColor
        }
        val noticePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = sansTypeface
            textSize = 9.5f
            color = noticeTextColor
        }

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 0.8f
            color = lineColor
        }

        var pageNumber = 1
        var currentPage: PdfDocument.Page? = if (writePages) {
            pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
        } else null
        var canvas: Canvas? = currentPage?.canvas
        var y = margin

        fun nextPage() {
            if (writePages && currentPage != null) {
                pdfDocument.finishPage(currentPage)
            }
            pageNumber++
            if (writePages) {
                currentPage = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
                canvas = currentPage?.canvas
            }
            y = margin
        }

        fun ensureSpace(neededHeight: Float) {
            if (y + neededHeight > bottomLimit) {
                nextPage()
            }
        }

        fun buildLayout(text: CharSequence, paint: TextPaint, width: Int): StaticLayout {
            return StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(2f, 1.15f)
                .setIncludePad(false)
                .build()
        }

        fun drawTextBlock(text: CharSequence, paint: TextPaint, spaceAfter: Float = 6f) {
            val layout = buildLayout(text, paint, contentWidth)
            ensureSpace(layout.height + spaceAfter)
            canvas?.let { c ->
                c.save()
                c.translate(margin, y)
                layout.draw(c)
                c.restore()
            }
            y += layout.height + spaceAfter
        }

        fun drawNoticeBox(text: String, spaceAfter: Float = 10f) {
            val innerWidth = contentWidth - 24
            val layout = buildLayout(text, noticePaint, innerWidth)
            val boxHeight = layout.height + 16f
            ensureSpace(boxHeight + spaceAfter)
            canvas?.let { c ->
                fillPaint.color = noticeBgColor
                c.drawRect(margin, y, margin + contentWidth, y + boxHeight, fillPaint)
                fillPaint.color = noticeBorderColor
                c.drawRect(margin, y, margin + 3.5f, y + boxHeight, fillPaint)
                c.save()
                c.translate(margin + 14f, y + 8f)
                layout.draw(c)
                c.restore()
            }
            y += boxHeight + spaceAfter
        }

        // Logo on Page 1
        logoBitmap?.let { bmp ->
            val targetW = 105f
            val targetH = targetW * bmp.height.toFloat() / bmp.width.toFloat()
            ensureSpace(targetH + 8f)
            canvas?.drawBitmap(bmp, null, RectF(margin, y, margin + targetW, y + targetH), null)
            y += targetH + 10f
        }

        drawTextBlock("Egyéni értékelőlap", h2Paint, 2f)
        drawTextBlock("Nevelőszülői SJT – Hazudozás és szabályszegés", mutedPaint, 10f)
        drawTextBlock("Kitöltési azonosító · ${report.participantName}", boldBodyPaint, 2f)
        drawTextBlock("Kitöltés dátuma: ${report.formattedDate}", mutedPaint, 8f)
        drawTextBlock("${report.totalScore} / ${report.maxTotalScore} pont", scorePaint, 10f)

        drawNoticeBox(
            "Nem validált teszt. Önálló kiválasztási vagy alkalmassági döntésre nem alkalmas. " +
                "Az eredmény azt mutatja, mennyire egyeznek a válaszok az előzetes szakmai kulccsal. " +
                "Nincs megfelelési küszöb vagy összehasonlítási norma."
        )

        drawTextBlock("Kompetenciaprofil", h3Paint, 2f)
        drawTextBlock("A részpontszámok területenként 2–3 helyzetből állnak össze.", mutedPaint, 6f)

        // 2x2 Competency grid
        val colGap = 10f
        val colWidth = ((contentWidth - colGap) / 2f).toInt()
        report.competencyProfile.chunked(2).forEach { row ->
            val cardHeights = row.map { area ->
                val nameLayout = buildLayout(area.area.name, boldBodyPaint, colWidth - 16)
                nameLayout.height + 34f
            }
            val rowHeight = cardHeights.maxOrNull() ?: 60f
            ensureSpace(rowHeight + 8f)
            row.forEachIndexed { idx, area ->
                val cardX = margin + idx * (colWidth + colGap)
                canvas?.let { c ->
                    c.drawRoundRect(
                        RectF(cardX, y, cardX + colWidth, y + rowHeight),
                        6f, 6f, strokePaint
                    )
                    val nameLayout = buildLayout(area.area.name, boldBodyPaint, colWidth - 16)
                    c.save()
                    c.translate(cardX + 8f, y + 8f)
                    nameLayout.draw(c)
                    c.restore()

                    val scoreY = y + 12f + nameLayout.height
                    c.drawText("${area.score} / ${area.maxScore} pont", cardX + 8f, scoreY + 8f, mutedPaint)

                    val barY = scoreY + 14f
                    val barW = colWidth - 16f
                    fillPaint.color = lineColor
                    c.drawRoundRect(RectF(cardX + 8f, barY, cardX + 8f + barW, barY + 5f), 2.5f, 2.5f, fillPaint)
                    fillPaint.color = greenColor
                    c.drawRoundRect(
                        RectF(cardX + 8f, barY, cardX + 8f + barW * area.ratio, barY + 5f),
                        2.5f, 2.5f, fillPaint
                    )
                }
            }
            y += rowHeight + 8f
        }

        y += 4f
        drawTextBlock("Fejlesztési visszajelzés", h3Paint, 4f)
        if (report.needsSafetyConsultation) {
            drawNoticeBox(
                "Beszélje át a 8. és a 10. helyzetet szakmai konzultáción. " +
                    "Gyakorolja a közvetlen veszély megszakítását és a bántalmazásról szóló közlésre adott első lépéseket."
            )
        }
        if (report.priorities.isNotEmpty()) {
            report.priorities.forEach { priority ->
                drawTextBlock("• ${priority.area.name}. ${priority.area.practice}", bodyPaint, 5f)
            }
        } else {
            drawTextBlock(
                "Minden választása egyezett a kulccsal. Képzésen gyakorolja, hogyan mondaná el a gyermeknek a választott reakció első mondatát, és mi lenne a következő lépése.",
                bodyPaint,
                6f
            )
        }

        y += 6f
        drawTextBlock("Visszajelzés helyzetenként", h3Paint, 6f)

        report.itemEvaluations.forEach { eval ->
            val sb = StringBuilder()
            sb.append("${eval.itemIndex + 1}. ${eval.item.title} · ${eval.totalPoints}/9 pont\n")
            sb.append("Legmegfelelőbbnek jelölte (${eval.bestPoints}/6 pont): ${eval.describeRank(eval.bestRank)}\n")
            sb.append("Legkevésbé megfelelőnek jelölte (${eval.worstPoints}/3 pont): ${eval.describeRank(eval.worstRank)}\n")
            if (eval.worstRank != eval.item.worstKeyRank) {
                sb.append("A kulcs szerinti legkevésbé megfelelő: ${eval.describeRank(eval.item.worstKeyRank)}\n")
            }
            if (eval.bestRank != 3) {
                sb.append("A kulcs szerinti legmegfelelőbb: ${eval.describeRank(3)}\n")
            }
            sb.append(eval.item.why)

            val layout = buildLayout(sb.toString(), bodyPaint, contentWidth)
            ensureSpace(layout.height + 10f)
            canvas?.let { c ->
                c.save()
                c.translate(margin, y)
                layout.draw(c)
                c.restore()
            }
            y += layout.height + 10f
        }

        y += 4f
        drawTextBlock("Pontozás", h3Paint, 4f)
        drawTextBlock(
            "A legmegfelelőbbnek jelölt reakció 0, 2, 4 vagy 6 pontot ér a szakmai kulcs szerint. " +
                "A kulcs szerinti legkevésbé megfelelő reakció felismerése további 3 pontot ér, minden más jelölés ebben a kategóriában 0 pontot. " +
                "Helyzetenként legfeljebb 9, összesen 90 pont szerezhető.",
            bodyPaint,
            6f
        )
        drawTextBlock(
            "A tételek és a kulcs saját fejlesztésűek. A szemlélethez felhasznált háttéranyagok az NCTSN nevelőszülői tananyaga és az NSPCC útmutatója a bántalmazásról szóló közlések fogadásához.",
            mutedPaint,
            4f
        )

        if (writePages && currentPage != null) {
            pdfDocument.finishPage(currentPage)
        }
        return pageNumber
    }
}
