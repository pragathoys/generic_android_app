package com.pragathoys.lib.controllers;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marjohn
 */
public class Crud {
    
    private Db db;
    String tables[];
    String db_name;
    Activity activity;

    public  final static int MODE_NEW = 1;
    public  final static int MODE_UPDATE = 2;
    
    public Crud(String db_name,Activity activity){
        this.db_name = db_name;
        this.activity = activity;
        
        tables= new String[]{
            "projects"
        };
    }
    
    public Cursor list(String table_name){
        Cursor c;
        db = new Db(db_name, activity);
        c = db.select("SELECT * FROM " + table_name);
        db.close();
        
        return c;
    }

    public Cursor list(String table_name,String[] fields, String[] values){
        Cursor c;
        db = new Db(db_name, activity);
        String filter_list = "";
        for(int i=0;i<fields.length;i++){
            if(filter_list == ""){
                filter_list = fields[i] + " LIKE '%" + values[i] + "%'";
            }else{
                filter_list += " OR " + fields[i] + " LIKE '%" + values[i] + "%'";
            }
        }        
        c = db.select("SELECT * FROM " + table_name + " WHERE " + filter_list);
        db.close();
        
        return c;
    }

    public Cursor list_exclusive(String table_name,String[] types,String[] fields, String[] values){
        Cursor c;
        db = new Db(db_name, activity);
        String filter_list = "";
        for(int i=0;i<fields.length;i++){
            Log.d("CRUD", "Type : " + types[i]);
            if(filter_list.equals("")){
                if(types[i].equals("str")){
                    filter_list = fields[i] + " LIKE ?";
                }else{
                    filter_list = fields[i] + "=?";
                }
            }else{
                
                if(types[i].equals("str")){
                    filter_list += " AND " + fields[i] + " LIKE ?";
                }else{
                    filter_list += " AND " + fields[i] + "=?";
                }                
            }
        }        
        String sql = "SELECT * FROM " + table_name + " WHERE " + filter_list;
        Log.d("CRUD", sql);
        
        c = db.select_args(sql,values);
        db.close();
        
        return c;
    }

    
    public Cursor load(String table_name, long id){
        Cursor c;
        db = new Db(db_name, activity);
        c = db.select("SELECT * FROM " + table_name + " WHERE _id=" + id);
        db.close();
        
        return c;
    }       
    
    public boolean save_image(String table_name,long project_id, Bitmap image,String mCurrentPhotoPath){
        boolean result = true;
        db = new Db(db_name, activity);
        ContentValues cv = new ContentValues();
        cv.put("thumb", getBytes(image));
        cv.put("filename", mCurrentPhotoPath);
        
        db.insert_image(table_name,cv);
        db.close();
        return result;
    }     
    
    public static byte[] getBytes(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, MODE_NEW, stream);
        return stream.toByteArray();
    }
    
    public List<Bitmap> load_images(String table_name,long id){
        Cursor c;
        List<Bitmap> images = new ArrayList<Bitmap>() ;
        db = new Db(db_name, activity);
        c = db.select_images(table_name, new String[]{"thumb","filename"},"_id=?",new String[]{id+""});
        c.moveToFirst();
        int counter= 0;
        if(c.moveToFirst()){
        do {
            byte[] blob = c.getBlob(c.getColumnIndex("thumb"));
            images.add( BitmapFactory.decodeByteArray(blob, 0, blob.length) );
        }while(c.moveToNext());
        }
        db.close();
        return images;
    }
    
    public List<Integer> load_images_ids(String table_name,long id){
        Cursor c;
        List<Integer> ids = new ArrayList<Integer>() ;
        db = new Db(db_name, activity);
        c = db.select_images(table_name, new String[]{"_id","filename"},"_id=?",new String[]{id+""});
        c.moveToFirst();
        int counter= 0;
        if(c.moveToFirst()){
        do {            
            ids.add( c.getInt(c.getColumnIndex("_id")) );
        }while(c.moveToNext());
        }
        db.close();
        return ids;
    }    

    public boolean add(String table_name,String[] fields, String[] values){
        boolean result = true;
        db = new Db(db_name, activity);
        String field_list = "", values_list="";
        for(int i=0;i<fields.length;i++){
            if(field_list == ""){
                field_list = fields[i];
            }else{
                field_list += "," + fields[i];
            }
        }
        
        for(int i=0;i<values.length;i++){
            if(values_list == ""){
                values_list = "'" + values[i] + "'";
            }else{
                values_list += ",'" + values[i] + "'";
            }
        }        
        db.insert(new String[]{"INSERT INTO " + table_name + "("+field_list+") VALUES("+values_list+");"});
        db.close();
        
        return result;
    }  
    
    public boolean update(String table_name,long id,String[] fields, String[] values){
        boolean result = true;
        db = new Db(db_name, activity);
        String field_list = "";
        for(int i=0;i<fields.length;i++){
            if(field_list == ""){
                field_list = fields[i] + "='" + values[i] + "'";
            }else{
                field_list += "," + fields[i] + "='" + values[i] + "'";
            }
        }
        
        db.update(new String[]{"UPDATE " + table_name + " set "+field_list + " WHERE _id=" + id +" ;"});
        db.close();
        
        return result;
    }               
    
    public boolean delete(String table_name,long id){
        boolean result = true;
        db = new Db(db_name, activity);
        db.update(new String[]{"DELETE FROM " + table_name + " WHERE _id=" + id + ";"});
        db.close();

        return result;
    }
    
    public boolean delete_related(String table_name,String related_column,long id){
        boolean result = true;
        db = new Db(db_name, activity);
        db.update(new String[]{"DELETE FROM " + table_name + " WHERE " + related_column+ "=" + id + ";"});
        db.close();

        return result;
    }    
}