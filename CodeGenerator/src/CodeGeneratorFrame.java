import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class CodeGeneratorFrame {

	public CodeGeneratorFrame() {
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 730, 489);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Generate Your code");
		setFields(frame);
		frame.setVisible(true);
	}

	private void setFields(JFrame frame) {
		final JTextField textField = new JTextField();
		textField.setText("250");
		textField.setBounds(128, 28, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		final JLabel lblHeight = new JLabel("Height");
		lblHeight.setBounds(65, 31, 46, 14);
		frame.getContentPane().add(lblHeight);

		final JTextField textField1 = new JTextField();
		textField1.setText("500");
		textField1.setBounds(128, 68, 86, 20);
		frame.getContentPane().add(textField1);
		textField1.setColumns(10);

		final JLabel lblWidth = new JLabel("Width");
		lblWidth.setBounds(65, 68, 46, 14);
		frame.getContentPane().add(lblWidth);

		JLabel lblCode = new JLabel("Type Of Code");
		lblCode.setBounds(65, 128, 86, 14);
		frame.getContentPane().add(lblCode);

		final JRadioButton radioButton = new JRadioButton("QrCode");
		radioButton.setSelected(true);
		radioButton.setBounds(337, 128, 109, 23);
		frame.getContentPane().add(radioButton);

		JRadioButton radioButton1 = new JRadioButton("BarCode");
		radioButton1.setBounds(162, 128, 109, 23);
		frame.getContentPane().add(radioButton1);

		ButtonGroup group = new ButtonGroup();
		group.add(radioButton);
		group.add(radioButton1);

		JRadioButton radioButton2 = new JRadioButton("In a PDF file");
		radioButton2.setBounds(162, 224, 109, 23);
		frame.getContentPane().add(radioButton2);

		JButton btnSubmit = new JButton("Generate");

		btnSubmit.setBackground(Color.GREEN);
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setBounds(65, 387, 89, 23);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String qrCodeData = String.valueOf(Math.random());
				String filePath = "YourCode.png";
				String pdfPath = "YourCode.pdf";
				
				//validating values
				if(!validate(textField.getText())){
					lblHeight.setForeground(Color.RED);
					return;
				}
				if(!validate(textField1.getText())){
					lblWidth.setForeground(Color.RED);
					return;
				}
				try {
					if (radioButton.isSelected())
						createQRBarCode(qrCodeData, filePath, Integer.parseInt(textField.getText()),
								Integer.parseInt(textField1.getText()), BarcodeFormat.QR_CODE,
								radioButton2.isSelected(), pdfPath);
					else
						createQRBarCode(qrCodeData, filePath, Integer.parseInt(textField.getText()),
								Integer.parseInt(textField1.getText()), BarcodeFormat.CODE_128,
								radioButton2.isSelected(), pdfPath);
				} catch (WriterException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}
			private boolean validate(String text) {
			      try {
			         Integer.parseInt(text);
			         return true;
			      } catch (NumberFormatException e) {
			         return false;
			      }
			   }
		});
		frame.getContentPane().add(btnSubmit);

	}

	public static void main(String[] args)
			throws WriterException, IOException, NotFoundException, InterruptedException {
		new CodeGeneratorFrame();
	}

	public static void createQRBarCode(String qrCodeData, String imagePath, int qrCodeheight, int qrCodewidth,
			BarcodeFormat barcodeFormat, boolean pdf, String pdfPath)
			throws WriterException, IOException, DocumentException {
		String charset = "UTF-8";
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
				barcodeFormat, qrCodewidth, qrCodeheight, hintMap);
		MatrixToImageWriter.writeToFile(matrix, imagePath.substring(imagePath.lastIndexOf('.') + 1),
				new File(imagePath));
		File file = new File(imagePath);
		Desktop.getDesktop().open(file);
		if (pdf) {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(new File(pdfPath)));
			document.open();
			document.newPage();
			Image image = Image.getInstance(imagePath);
			image.setAbsolutePosition(0, 0);
			image.setBorderWidth(0);
			image.scaleAbsolute(PageSize.A4);
			document.add(image);
			document.close();
			file = new File(pdfPath);
			Desktop.getDesktop().open(file);
		}
	}

}
