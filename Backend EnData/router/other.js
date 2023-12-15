const express = require('express')
const router = express.Router()
const pool = require('../database/koneksi')
const moment = require('moment');
module.exports = router


router.get('/riwayatprivate/:id_user', async(req, res)=>{
	const {id_user} = req.params
	let connection
	let sql
	try {
		connection = await pool.getConnection()
		
		sql = `SELECT id as id_file, nama_file, description, created, is_send FROM files WHERE id_user = '${id_user}'`
		const [result] = await connection.query(sql, [], {nestTables : false, rowsAsArray :false})
		if(result==0){
			throw new Error('Belum ada riwayat')
		}
		const newResult = result.map((item) => {
			return {
			  ...item,
			  extension: item.nama_file.split('.')[1],
			  created : moment(item.created).format('DD-MM-YYYY')
			};
		  });
		res.json({
			success : true,
			message : 'Berhasil mengambil data riwayat',
			newResult
		})
		
	} catch (error) {
		res.json({
			success : false,
			message : error.message
		})
	}
})


router.get('/riwayatprivate/:extension/:id_user', async(req, res)=>{
	const {id_user, extension} = req.params
	let connection
	let sql
	try {
		connection = await pool.getConnection()
		sql = `SELECT id as id_file, nama_file, description, created, token FROM files WHERE id_user = '${id_user}'`
		const [result] = await connection.query(sql, [], {nestTables : false, rowsAsArray :false})
		if(result==0){
			throw new Error('Belum ada riwayat')
		}
		const newResult = result.map((item) => {
			return {
			  ...item,
			  extension: item.nama_file.split('.')[1],
			  created : moment(item.created).format('DD-MM-YYYY')
			};
		  });

		let filteredFiles
		if(extension=="png"){
			const supportedFormats = extension === "png" ? ["png", "jpg", "jpeg"] : [extension];
			filteredFiles = newResult.filter((file) =>
				supportedFormats.includes(file.extension)
			);
			if(filteredFiles==0){
				throw new Error('File tidak titemukan')
			} 
		}else{
			filteredFiles = newResult.filter((file) => file.extension === extension);
			if(filteredFiles==0){
				throw new Error('File tidak titemukan')
			}
		}
		res.json({
			success : true,
			message : 'Berhasil mengambil data riwayat',
			filteredFiles
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
		sql = `SELECT files.nama_file, users.nama as nama_pengirim, files.created FROM files JOIN users ON files.id_user_send = users.id WHERE id_user = ${id_user} AND is_send = 1`
		const [result] = await connection.query(sql, [], {nestTables :false, rowsAsArray :false})
		if(result==0){
			throw new Error('Kotak masuk kosong')
		}
		const newResult = result.map((item) => {
			return {
			  ...item,
			  created : moment(item.created).format('DD-MM-YYYY')
			};
		  });
		res.json({
			success : true,
			message : 'Berhasil mendapatkan data kotak masuk',
			newResult
		})
		
	} catch (error) {
		res.json({
			success : false,
			message : error.message
		})
	}
})


router.get('/profil/:id_user', async(req, res)=>{
	const {id_user} = req.params
	let connection
	let sql
	try {
		connection = await pool.getConnection()
		sql = `SELECT nama,email FROM users WHERE id = ${id_user}`
		const [result] = await connection.query(sql, [], {nestTables :false, rowsAsArray :false})
		res.json({
			success : true,
			message : 'Berhasil mendapatkan data profil',
			result
		})
		
	} catch (error) {
		res.json({
			success : false,
			message : error.message
		})
	}
})