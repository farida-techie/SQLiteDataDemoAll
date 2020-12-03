package com.malkinfo.simplesqleitedemo

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.malkinfo.simplesqleitedemo.data.HelperSQLite
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.listv.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var helperSQL:HelperSQLite
    private lateinit var db:SQLiteDatabase
    private lateinit var rs:Cursor
    private lateinit var adapter :SimpleCursorAdapter
    private lateinit var dailogB:AlertDialog.Builder
    private lateinit var listBliud :View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addSQLite()

    }
    fun addSQLite(){
        /**call the object*/
         helperSQL = HelperSQLite(application)
         db  = helperSQL.readableDatabase
         rs = db.rawQuery("SELECT * FROM ACTABLE ORDER BY NAME",null)



        /**set Button View All*/
        btnViewALL.setOnClickListener {
            listBliud = LayoutInflater.from(this).inflate(R.layout.listv,null)

            /**set Logan Click item in List View show the Dialog Is Delete */
            registerForContextMenu(listBliud.listV)
            dailogB = AlertDialog.Builder(this)
            dailogB.setView(listBliud)
            dailogB.setTitle("List SQLite Data")

            /**set Adapter in List View*/
             adapter = SimpleCursorAdapter(this,
                    android.R.layout.simple_expandable_list_item_2,
                    rs, arrayOf("NAME","MEANING"),
                    intArrayOf(android.R.id.text1,android.R.id.text2),0)
            listBliud.listV.adapter = adapter
            adapter.notifyDataSetChanged()


            /**View All Data in Search View*/
            listBliud.seach.queryHint = "Search Among ${rs.count} Record"
            listBliud.seach.isIconified = true

            /**set searchingBar is text */
            listBliud.seach.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    rs = db.rawQuery("SELECT *FROM ACTABLE WHERE NAME LIKE '%${newText}%' OR MEANING LIKE '%${newText}%'",null)
                    adapter.changeCursor(rs)


                   return false
                }

            })
            dailogB.show()
        }





        /**set First Data in Table*/
        btnFirst.setOnClickListener {
            if (rs.moveToFirst()){
                etText.setText(rs.getString(1))
                etText0.setText(rs.getString(2))
            }else{
                Toast.makeText(this,"Data is not Found",Toast.LENGTH_SHORT).show()
            }
        }

        /**set button Next Data */
        btnNext.setOnClickListener {
            if (rs.moveToNext()){
                etText.setText(rs.getString(1))
                etText0.setText(rs.getString(2))
            }else if (rs.moveToFirst()){
                etText.setText(rs.getString(1))
                etText0.setText(rs.getString(2))
            }else{
                Toast.makeText(this,"Data is not Found",Toast.LENGTH_SHORT).show()
            }

        }
        /** set Delete Data*/
        btnDelete.setOnClickListener {
            db.delete("ACTABLE","_id =?", arrayOf(rs.getString(0)))
            rs.requery()
            /**set Dailog Box*/
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Delete Record")
            ad.setMessage("Record Delete successfully...!!")
            ad.setPositiveButton("OK",DialogInterface.OnClickListener { dialog, which ->
                if (rs.moveToFirst()){
                    etText.setText(rs.getString(1))
                    etText0.setText(rs.getString(2))
                    etText.requestFocus()

                }else{
                    etText.setText("No Data Found")
                    etText0.setText("No Data Found")
                    etText.requestFocus()
                }

            })
            ad.show()
        }

        /**set Update data*/
        btnUpdate.setOnClickListener {
            var cv = ContentValues()
             cv.put("NAME",etText.text.toString())
            cv.put("MEANING",etText0.text.toString())
            db.update("ACTABLE",cv,"_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            /**set Dailog Box*/
            var ad = AlertDialog.Builder(this)
            ad.setTitle("UpDate Record")
            ad.setMessage("Record UpDate successfully...!!")
            ad.setPositiveButton("OK",DialogInterface.OnClickListener { dialog, which ->
                if (rs.moveToFirst()){
                    etText.setText(rs.getString(1))
                    etText0.setText(rs.getString(2))
                    etText.requestFocus()

                }

            })
            ad.show()
        }

        /**set Button Previous data*/
        btnPrev.setOnClickListener {
            if (rs.moveToPrevious()){
                etText.setText(rs.getString(1))
                etText0.setText(rs.getString(2))
            }else if (rs.moveToLast()){
                etText.setText(rs.getString(1))
                etText0.setText(rs.getString(2))
            }else{
                Toast.makeText(this,"Data is not Found",Toast.LENGTH_SHORT).show()
            }

        }

        /**set Button Last*/
        btnLast.setOnClickListener {
            if (rs.moveToLast()){
                etText.setText(rs.getString(1))
                etText0.setText(rs.getString(2))
            }else{
                Toast.makeText(this,"Data is not Found",Toast.LENGTH_SHORT).show()
            }

        }

        /**set Insert Data */
        btnInsert.setOnClickListener {
            /**add the data in SQLiteData*/
            var cv = ContentValues()
            cv.put("NAME",etText.text.toString())
            cv.put("MEANING",etText0.text.toString())
            db.insert("ACTABLE",null,cv)
            rs.requery()
            /**set Dialog Box*/
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Add Record")
            ad.setMessage("Record Inserted successfully...!!")
            ad.setPositiveButton("OK",DialogInterface.OnClickListener { dialog, which ->
                etText.setText("")
                etText0.setText("")
                etText.requestFocus()
            })
            ad.show()
        }

        /**set Button Clear*/
        btnClear.setOnClickListener {
            etText.setText("")
            etText0.setText("")
            etText.requestFocus()
        }


    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(101,11,1,"Delete")
        menu?.setHeaderTitle("Removing Data")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        /**set the delete*/
        if (item.itemId != 11){
            db.delete("ACTABLE","_id=?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            Toast.makeText(this,"Record Delete successfully...!!",Toast.LENGTH_LONG).show()
        }

        return super.onContextItemSelected(item)
    }
}