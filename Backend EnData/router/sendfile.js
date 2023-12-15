const express = require('express')
const router = express.Router()
const pool = require('../database/koneksi')
const encryption = require('../function/encryption')
const fs = require('fs');
const path = require('path');
const email = require('../function/email')
module.exports = router

router.post('/sendfile', async(req, res)=>{
	const {id_user, id_user_send, filename, description, base64} = req.body
	let connection
	let sql
	try {
		connection = await pool.getConnection()
		sql = `SELECT * FROM users WHERE id = ${id_user_send}`
		const [result] = await connection.query(sql, [], {nestTables :false, rowsAsArray : false})
		if(result==0){
			throw new Error('Rumah sakit tujuan tidak ditemukan')
		}

		const namaBaru = filename.replace(/\s/g, "-");
		const [token, iv] = await encryption.encrypt(base64)
		const date = Date.now() + `-${namaBaru}`
		fs.writeFileSync(path.join(__dirname + `/../files/encryption-file/${date}`),token.toString())
		sql = `INSERT INTO files (id_user, nama_file, description, token, path) VALUES (${id_user}, '${filename}', '${description}', '${iv}','${date}' ) `
		await connection.query(sql)
		//insert to sender
		sql = `INSERT INTO files (id_user, id_user_send, nama_file, description, token, path, is_send) VALUES ( ${id_user_send}, ${id_user},'${filename}', '${description}', '${iv}','${date}', 1 ) `
		await connection.query(sql)

		sql = `SELECT * FROM users WHERE id = ${id_user}`
		const [resultSender] = await connection.query(sql, [], {nestTables : false, rowsAsArray :false})

		// await email.sendEmail(result[0].email, result[0].nama, iv,resultSender[0].nama )
		res.json({
			success : true,
			message : 'Kirim file berhasil'
		})

	} catch (error) {
		res.json({
			success : false,
			message : error.message
		})
	}
})

router.get('/kotakmasuk/:id_user', async(req, res)=>{
	const {id_user} = req.params
	let connection
	let sql
	try {
		connection = await pool.getConnection()
		sql = `SELECT * FROM files WHERE id_user_send = ${id_user} AND is_send = 1`
		const [result] = await connection.query(sql, [], {nestTables :false, rowsAsArray :false})
		if(result==0){
			throw new Error('Belum ada file yang dikirim')
		}
		res.json({
			success : true,
			message : 'Berhasil mendapatkan data kotak masuk',
			result
		})
		
	} catch (error) {
		res.json({
			success : false,
			message : error.message
		})
	}
})

router.get('/daftarrumahsakit', async(req, res)=>{
	let connection
	let sql
	try {
		connection = await pool.getConnection()
		sql = `SELECT id, nama FROM users`
		const [result] = await connection.query(sql, [], {nestTables :false, rowsAsArray :false})
		res.json({
			success : true,
			message : 'Berhasil mendapatkan daftar rumah sakit',
			result
		})
		
	} catch (error) {
		res.json({
			success : false,
			message : error.message
		})
	}
})