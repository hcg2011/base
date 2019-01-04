package com.chungo.base.utils

import android.content.Context
import android.util.Log
import java.io.*
import java.nio.charset.Charset
import java.util.zip.*

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/12/11 13:41
 */
class ZipUtils {

    /**
     * 在/data/data/下创建一个res文件夹，存放4个文件
     */
    fun copyDbFile(context: Context, fileName: String) {
        lateinit var input: InputStream
        lateinit var out: FileOutputStream
        val path = "/data/data/" + context.packageName + "/file/res/"
        val file = File(path + fileName)

        //创建文件夹
        val filePath = File(path)
        if (!filePath.exists())
            filePath.mkdirs()

        if (file.exists())
            return
        input = context.assets.open(fileName) // 从assets目录下复制
        out = FileOutputStream(file)
        var length = -1
        val buf = ByteArray(1024)
        input.use { inputs ->
            out.use {
                while (input.read(buf).also { length = it } != -1) {
                    it.write(buf, 0, length)
                }
                it.flush()
            }
        }
    }

    @Synchronized
    fun compressFile(zipFile: String, targetDir: String) {
        val path = "/data/data/" + "/file/" //压缩到此处
        val file = File(path + "res.zip")  //要压缩的文件的路径
        val filePath = File(path)   //创建文件夹
        if (!filePath.exists())
            filePath.mkdirs()

        if (file.exists())
            return

        try {
            val zipOutputStream = ZipOutputStream(CheckedOutputStream(FileOutputStream(file), CRC32()))
            zip(zipOutputStream, "res", File(path + "res/"))
            zipOutputStream.flush()
            zipOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.d(TAG, "压缩完成")
    }

    @Throws(IOException::class)
    fun zip(out: ZipOutputStream, name: String, fileSrc: File) {
        var name = name

        if (fileSrc.isDirectory) {
            println("需要压缩的地址是目录")
            val files = fileSrc.listFiles()

            name = "$name/"
            out.putNextEntry(ZipEntry(name))  // 建一个文件夹
            println("目录名: $name")

            for (f in files) {
                zip(out, name + f.name, f)
                println("目录: " + name + f.name)
            }

        } else {
            println("需要压缩的地址是文件")
            out.putNextEntry(ZipEntry(name))
            println("文件名: $name")
            val input = FileInputStream(fileSrc)
            println("文件路径: $fileSrc")
            val buf = ByteArray(1024)
            var len = -1
            input.use { inputs ->
                out.use {
                    while (input.read(buf).also { len = it } != -1) {
                        it.write(buf, 0, len)
                    }
                    it.flush()
                }
            }
        }
    }


    @Synchronized
    fun unZip2(zipPath: String, targetDir: String): File? {

        val file = upZipFile(zipPath, targetDir)
        if (file == null)
            Log.d(TAG, "解压 $zipPath 错误!")
        return file
    }

    /**
     * 解压缩
     * 将zipFile文件解压到folderPath目录下.
     *
     * @param zipFile    zip文件
     * @param folderPath 解压到的地址
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun upZipFile(zipFile: String, folderPath: String): File? {
        val zfile = ZipFile(zipFile)
        var file: File? = null
        var ze: ZipEntry
        val buf = ByteArray(1024)
        var i = 0
        zfile.use {
            val zList = zfile.entries()
            while (zList.hasMoreElements()) {
                ze = zList.nextElement() as ZipEntry
                if (ze.isDirectory) {
                    var dirstr = folderPath + "/" + ze.name
                    dirstr = String(dirstr.toByteArray(charset("8859_1")), Charset.forName("GB2312"))
                    Log.d(TAG, "str = $dirstr")
                    val f = File(dirstr)
                    f.mkdir()
                    if (i == 0)
                        file = f
                    i++
                    continue
                }
                val out = BufferedOutputStream(FileOutputStream(getRealFileName(folderPath, ze.name)))
                val input = BufferedInputStream(zfile.getInputStream(ze))
                var cache = -1
                input.use { inputs ->
                    out.use {
                        while (inputs.read(buf, 0, 1024).also { cache = it } != -1) {
                            it.write(buf, 0, cache)
                        }
                        it.flush()
                    }
                }
            }

        }
        return file
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    protected fun getRealFileName(baseDir: String, absFileName: String): File {
        val dirs = absFileName.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var ret = File(baseDir)
        var substr: String? = null
        if (dirs.size > 1) {
            for (i in 0 until dirs.size - 1) {
                substr = dirs[i]
                try {
                    substr = String(substr.toByteArray(charset("8859_1")), Charset.forName("GB2312"))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }

                ret = File(ret, substr!!)

            }
            Log.d(TAG, "1ret = $ret")
            if (!ret.exists())
                ret.mkdirs()
            substr = dirs[dirs.size - 1]
            try {
                substr = String(substr.toByteArray(charset("8859_1")), Charset.forName("GB2312"))
                Log.d(TAG, "substr = $substr")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            ret = File(ret, substr!!)
            Log.d(TAG, "2ret = $ret")
            return ret
        }
        return ret
    }

    companion object {
        val TAG = "ZipUtils"
    }
}
