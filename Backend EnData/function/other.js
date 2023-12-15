const path = require('path')
const QRCode = require('qrcode');

exports.generateQRCode = async (data, token) => {
	try {
		const qrCodeOptions = {
		errorCorrectionLevel: 'H', // Level koreksi kesalahan QR code (L, M, Q, H)
		type: 'image/png', // Format output gambar (PNG, JPEG, SVG, EPS)
		quality: 0.92, // Kualitas gambar (0.0 - 1.0)
		margin: 1, // Margin putih di sekeliling QR code (unit: modul QR code)
		};

		const qrCodeFilePath = path.join(__dirname, `../files/qr_code/${data}.png`);
    	await QRCode.toFile(qrCodeFilePath, token, qrCodeOptions);

		console.log('QR code berhasil dihasilkan.');
		console.log('Data URL QR code:', qrCodeFilePath);
		return data
	} catch (error) {
		console.error('Error generating QR code:', error);
	}
};