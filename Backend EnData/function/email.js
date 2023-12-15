const nodemailer = require('nodemailer');
const mustache = require('mustache');
const encryption = require('../function/encryption')
const otherFunction = require('../function/other')
const fs = require('fs');

exports.sendEmail = async (email, name, token, sender) => {
	return new Promise(async (resolve) => {
	  let transporter = nodemailer.createTransport({
		host: 'srv42.niagahoster.com',
		port: 465,
		secure: true,
		auth: {
		  user: 'endata@faiznazhir.com',
		  pass: '@U-xWLQ]S9W9'
		}
	  });
  
	  try {
		const random = await encryption.randomString(10); // Generate random string
		const resultQR = await otherFunction.generateQRCode(random, token); // Generate QR code
  
		// Read the email template file asynchronously
		fs.readFile('./files/template/email.html', 'utf-8', async (err, template) => {
		  if (err) {
			console.error('Error reading email template:', err);
			resolve(false);
			return;
		  }
  
		  const qrCodePath = `./files/qr_code/${resultQR}.png`; // Path to the QR code file
  
		  try {
			await fs.promises.access(qrCodePath, fs.constants.F_OK); // Verify if the QR code file exists in the specified directory
		  } catch (error) {
			console.error('QR code file does not exist:', error);
			resolve(false);
			return;
		  }
  
		  // setup email data with unicode symbols
		  let mailOptions = {
			from: '"Endata" <endata@faiznazhir.com>', // sender address
			to: email, // recipient email
			subject: 'Kode Token from Endata', // subject line
			priority: 'high', // Menambahkan prioritas tinggi
			headers: {
				'X-Priority': '1', // Memberikan nilai 1 pada header X-Priority (1 adalah prioritas tertinggi)
			},
			html: mustache.render(template, { name: name, token: token, sender : sender}), // HTML body with template variables
			attachments: [
			  {
				filename: 'image.png', // Specify the filename for the attachment
				path: qrCodePath, // Path to the QR code file
				cid: 'image' // Unique content ID for the image
			  }
			]
		  };
  
		  // send mail with defined transport object
		  transporter.sendMail(mailOptions, (error, info) => {
			if (error) {
			  console.error('Error sending email:', error);
			  resolve(false);
			} else {
			  console.log('Email sent: ' + info.response);
			  resolve(true);
			}
		  });
		});
	  } catch (error) {
		console.error('Error generating random string or QR code:', error);
		resolve(false);
	  }
	});
  };
  

  exports.sendEmailPrivate = async (email, name, token, nama_file) => {
	return new Promise(async (resolve) => {
	  let transporter = nodemailer.createTransport({
		host: 'srv42.niagahoster.com',
		port: 465,
		secure: true,
		auth: {
		  user: 'endata@faiznazhir.com',
		  pass: '@U-xWLQ]S9W9'
		}
	  });
  
	  try {
		const random = await encryption.randomString(10); // Generate random string
		const resultQR = await otherFunction.generateQRCode(random, token); // Generate QR code
  
		// Read the email template file asynchronously
		fs.readFile('./files/template/email-private.html', 'utf-8', async (err, template) => {
		  if (err) {
			console.error('Error reading email template:', err);
			resolve(false);
			return;
		  }
  
		  const qrCodePath = `./files/qr_code/${resultQR}.png`; // Path to the QR code file
  
		  try {
			await fs.promises.access(qrCodePath, fs.constants.F_OK); // Verify if the QR code file exists in the specified directory
		  } catch (error) {
			console.error('QR code file does not exist:', error);
			resolve(false);
			return;
		  }
  
		  // setup email data with unicode symbols
		  let mailOptions = {
			from: '"Endata" <endata@faiznazhir.com>', // sender address
			to: email, // recipient email
			subject: 'Kode Token from Endata', // subject line
			priority: 'high', // Menambahkan prioritas tinggi
			headers: {
				'X-Priority': '1', // Memberikan nilai 1 pada header X-Priority (1 adalah prioritas tertinggi)
			},
			html: mustache.render(template, { name: name, token: token, nama_file : nama_file}), // HTML body with template variables
			attachments: [
			  {
				filename: 'image.png', // Specify the filename for the attachment
				path: qrCodePath, // Path to the QR code file
				cid: 'image' // Unique content ID for the image
			  }
			]
		  };
  
		  // send mail with defined transport object
		  transporter.sendMail(mailOptions, (error, info) => {
			if (error) {
			  console.error('Error sending email:', error);
			  resolve(false);
			} else {
			  console.log('Email sent: ' + info.response);
			  resolve(true);
			}
		  });
		});
	  } catch (error) {
		console.error('Error generating random string or QR code:', error);
		resolve(false);
	  }
	});
  };
  


