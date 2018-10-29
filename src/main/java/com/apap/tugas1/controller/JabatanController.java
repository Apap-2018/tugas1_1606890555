package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.service.JabatanService;

@Controller
public class JabatanController {
	@Autowired
	private JabatanService jabatanService;
	
	//Menampilkan data suatu jabatan (fitur 6)
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	public String viewJabatan(@RequestParam(value = "id", required = true) Long id, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanById(id).get();
		model.addAttribute("jabatan", jabatan);
		
		return "viewJabatan";
	}
	
	//Menampilkan data seluruh jabatan (fitur 9)
	@RequestMapping("/jabatan/viewall")
	public String viewAllJabatan(Model model) {
		List<JabatanModel> jabatan = jabatanService.getAllJabatan();
		model.addAttribute("jabatan", jabatan);
		
		return "viewAllJabatan";
	}
	
	//Menambahkan jabatan (fitur 5)
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	public String addJabatanGET(Model model) {
		JabatanModel jabatanBaru = new JabatanModel();
		model.addAttribute("jabatan", jabatanBaru);
		model.addAttribute("header", "Tambah Jabatan");
		
		return "addJabatan";
	}
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	public String addJabatan(Model model, @ModelAttribute JabatanModel jabatan) {
		jabatanService.addJabatan(jabatan);
		
		return "add";
	}
	
	//Mengubah data jabatan (fitur 7)
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	public String updateJabatanGET(@RequestParam(value = "id", required = true) Long id, Model model) {
		JabatanModel jabatan= jabatanService.getJabatanById(id).get();
		model.addAttribute("jabatan", jabatan);
		
		return "updateJabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	public String updateJabatan(Model model, @ModelAttribute JabatanModel jabatan) {
		jabatanService.updateJabatan(jabatan.getId(), jabatan);
		
		return "update";
	}
	
	//Menghapus data jabatan (fitur 8)
	@RequestMapping(value = "jabatan/hapus", method = RequestMethod.GET)
	public String deleteJabatan(@RequestParam(value = "id", required = true) Long id, Model model) {
		jabatanService.deleteById(id);
		return "delete";
	}
	
}
