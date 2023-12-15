const express = require('express')
const router = express.Router()
const pool = require('../database/koneksi')
const bcrypt = require('bcrypt')
const saltRounds = 10;

module.exports = router


router.post('/register', async (req, res)=>{
	const {nama, email, password} = req.body
	let connection
	try{
		connection = await pool.getConnection()
		const hashPassword = await bcrypt.hash(password, saltRounds)
		let sql = `SELECT email FROM users WHERE email =  '${email}'`
		const[result] = await connection.query(sql, [], {rowsAsArray: false, nestTables: false})
		console.log(result)
		if(result.length!=0){
			throw new Error('Email sudah digunakan')
		}
		sql = `INSERT INTO users (nama, email, password) VALUES ('${nama}', '${email}', '${hashPassword}')`
		await connection.query(sql)
		res.json({
			success : true,
			message : 'Registrasi berhasil'
		})
	}catch(error){
		res.json({
			success : false,
			message : error.message
		})
	}finally{
		if (connection) {
			connection.release();
		  }
	}
})

router.post('/login', async (req, res)=>{
	const {email, password} = req.body
	let connection
	try{
		connection - await pool.getConnection()
		let sql = `SELECT * FROM users WHERE email = '${email}'`
		let [result] = await pool.query(sql, [], {rowsAsArray : false, nestTables :false})
		console.log()
		if(result.length == 0){
			throw new Error('User tidak ditemukan')
		}
		const isPasswordTrue = await bcrypt.compare(password,result[0].password)
		if(!isPasswordTrue){
			throw new Error('Password salah')
		}
		res.json({
			success : true,
			message : 'Login berhasil',
			id : result[0].id,
			email : result[0].email,
			})

	}
	catch(error){
		res.json({
			success : false,
			message : error.message
		})
	}finally{
		if(connection){
			connection.release()
		}
	}

})

router.put('editprofile/:id_user', async(req, res)=>{
	const {id_user} = req.params
	const{nama, alamat, image, nomor} = req.body

	let sql = `SELECT * FROM users WHERE id = ${id_user}`

	try{
		const result = await query(sql)
		let old_name = result[0].name
		let old_nomor = result[0].numphone
		let old_alamat = result[0].address
		let old_image = result[0].image

		if(!nama){
			console.log('ini')
			sql = `UPDATE users SET name = '${old_name} WHERE id = ${id_user}`
			await query(sql)
		}else{
			sql = `UPDATE users SET name = '${nama}' WHERE id = ${id_user}`
			await query(sql)
		}
		if(!nomor){
			sql = `UPDATE users SET numphone = '${old_nomor}' WHERE id = ${id_user}`
			await query(sql)
		}else{
			sql = `UPDATE users SET numphone = '${nomor}' WHERE id = ${id_user}`
			await query(sql)
		}
		if(!alamat){
			sql = `UPDATE users SET address = '${old_alamat}' WHERE id = ${id_user}`
			await query(sql)
		}else{
			sql = `UPDATE users SET address = '${alamat}' WHERE id = ${id_user}`
			await query(sql)
		}
		if(!image){
			sql = `UPDATE users SET image = '${old_image}' WHERE id = ${id_user}`
			await query(sql)
		}else{
			fs.writeFileSync(path.join(__dirname + `/../file/profile/${old_name+'.png'}`),image, 'base64')
			const url = `/public/profile/${old_name+'.png'}`
			sql = `UPDATE users SET image = '${url}' WHERE id = ${id_user}`
			await query(sql)
		}
		res.json({
			success : true,
			message : 'Edit profil berhasil',
		})
		
		
	}catch(error){
		res.json({
			success : false,
			message : error.message
		})
	
	}
})
