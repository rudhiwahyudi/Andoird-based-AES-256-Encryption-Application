const express = require('express')
const router = express.Router()
const pool = require('../database/koneksi')
const encryption = require('../function/encryption')
const fs = require('fs');
const path = require('path');
const email  = require('../function/email')
module.exports = router

router.post('/dekripsi', async(req, res)=>{
	const {id_user, token} = req.body
	let connection
	let sql
	try {
		connection = await pool.getConnection()
		sql = `SELECT * FROM files WHERE token = '${token}' AND id_user = ${id_user}`
		const [result] = await connection.query(sql, [], {nestTables : false, rowsAsArray : false})
		if(result==0){
			throw new Error('file tidak ditemukan')
		}
		const pathFile = result[0].path
		const keyVector = result[0].token
		if(keyVector!=token){
			throw new Error("Token salah")
		}
		const readFile = fs.readFileSync(path.join(__dirname + `/../files/encryption-file/${pathFile}`), 'utf8')
		const resultDecrypt = await encryption.decrypt(readFile, token)
		fs.writeFileSync(path.join(__dirname + `/../files/decryption-file/${pathFile.replace('.enc','')}`),resultDecrypt, 'base64')
		const url = `/public/decryption-file/${pathFile.replace('.enc','')}`
		res.json({
			success : true,
			message : 'Dekripsi berhasil',
			id_file : result[0].id,
			url : url
		})
		
	} catch (error) {
		res.json({
			success : false,
			message : error.message
		})
	}
})

router.post('/enkripsi', async(req, res)=>{
	const {id_user, filename, description, base64} = req.body
	let connection
	let sql
	try {
		connection = await pool.getConnection()
		sql = `SELECT * FROM users WHERE id = ${id_user}`
		const [result] = await connection.query(sql, [], {nestTables :false, rowsAsArray : false})
		if(result==0){
			throw new Error('User tidak ditemukan')
		}

		const namaBaru = filename.replace(/\s/g, "-");
		const [token, iv] = await encryption.encrypt(base64)
		const date = Date.now() + `-${namaBaru}`
		fs.writeFileSync(path.join(__dirname + `/../files/encryption-file/${date}`),token.toString())
		sql = `INSERT INTO files (id_user, nama_file, description, token, path) VALUES (${id_user}, '${namaBaru}', '${description}', '${iv}','${date}') `
		await connection.query(sql)
		await email.sendEmailPrivate(result[0].email, result[0].nama, iv, filename)
		res.json({
			success : true,
			message : 'Enkripsi file berhasil'
		})

	} catch (error) {
		res.json({
			success : false,
			message : error.message
		})
	}
})
