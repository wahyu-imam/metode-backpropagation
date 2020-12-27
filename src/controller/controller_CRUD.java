/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.utama;
/**
 *
 * @author Wahyu Imam
 */
public interface controller_CRUD {
    public void simpan(utama utama);
    public void ubah(utama utama);
    public void hapus(utama utama);
    public void tampil(utama utama);
    public void klikTabel(utama utama);
    public void bersih(utama utama);
}
