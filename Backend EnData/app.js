const express = require('express')
const app = express()
const http = require('http').createServer(app)
const auth = require('./router/auth')
const sendFile = require('./router/sendfile')
const encryption = require('./router/encryption')
const other = require('./router/other')
//middleware
app.use(express.json({limit:'50mb'}))
app.use(express.urlencoded({extended:true}))
app.use('/public', express.static('./files'))

//router
app.get('/', (req, res)=>{
	res.json({
		status : 'connected',
		message : 'EnData'
	})
})

app.use('/auth', auth)
app.use('/send', sendFile)
app.use('/proses', encryption)
app.use('/other', other)
http.listen(5000, ()=>{
	console.log('Server berjalan pada port 5000')
})