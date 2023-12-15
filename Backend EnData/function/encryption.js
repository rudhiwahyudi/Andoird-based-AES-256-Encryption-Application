const CryptoJS = require('crypto-js');
const crypto = require('crypto')
const iv = crypto.randomBytes(16); 
require('dotenv').config();
const key = process.env.KEY;

exports.encrypt =  async(plaintext)=>{
	// Encrypt
	const ciphertext = CryptoJS.AES.encrypt(plaintext, key, {
		mode: CryptoJS.mode.CBC, // mode CBC
		padding: CryptoJS.pad.Pkcs7, // padding PKCS7
		iv: iv, // set IV
		keySize: 256/32 // key size 256 bits

	  });

	return [ciphertext, iv.toString('hex')]

};


exports.decrypt = async (ciphertext, keyVector)=>{
	const bytes = CryptoJS.AES.decrypt(ciphertext.toString(), key, {
		mode: CryptoJS.mode.CBC, // mode CBC
		padding: CryptoJS.pad.Pkcs7, // padding PKCS7
		iv: keyVector, // set IV
		keySize: 256/32 // key size 256 bits
	  });

	  const plaintextDecrypted = bytes.toString(CryptoJS.enc.Utf8);
	  return plaintextDecrypted
};

exports.randomString = async(length)=>{
	try{
		const bytes = crypto.randomBytes(Math.ceil(length / 2));
		return bytes.toString('hex').slice(0, length);
	}catch(error){
		console.error('Error random string', error);
	  return null;
	}
}
