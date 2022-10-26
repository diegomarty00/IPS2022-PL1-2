package business.cita.operaciones;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hsqldb.lib.ArrayUtil;

import persistencia.cita.CitaRecord;
import persistencia.cita.MedicoCitaRecord;
import persistencia.cita.impl.CitaGatewayImpl;
import persistencia.cita.impl.MedicoCitaGatewayImpl;
import persistencia.medico.MedicoRecord;
import persistencia.medico.impl.MedicoGatewayImpl;
import persistencia.paciente.PacienteRecord;
import persistencia.paciente.impl.PacienteGatewayImpl;
import util.mail.EnviarMail;

public class CrearCita {
	 
	int nextid = nextId();
	CitaRecord ci = new CitaRecord();
	String nombre = "";
	 private int nextId() {
		 CitaGatewayImpl c = new CitaGatewayImpl();
		 List<CitaRecord> list = c.findAll();
		if(list.size() > 0) {
			
			return list.size()+1;
		}
		return 1;
	}
	
	private  String parsePaciente(String pa) {
		String dni = "";
		String[] partes = pa.split(" ");
		dni = partes[partes.length -1 ];
		for(int i = 0 ; i< partes.length -1 ;i++) {
			nombre = nombre +   partes[i] + " ";
		}
		return dni ;
	}
	private int parseMed(String s ) {
		int idm = -1;
		String[] partes = s.split(" ");
		idm = Integer.parseInt(partes[partes.length -1 ]);
		return idm; 
	}
	
	public PacienteRecord sacarDatosContacto(String dni){
		List<String> li = new ArrayList<>();
		PacienteGatewayImpl pa = new PacienteGatewayImpl();
		PacienteRecord p = pa.findById(dni).get();
		
		
		return p ; 
	}
	
	public void crearCita(String paciente, boolean urgencia,String lugar, String a�o , String mes , String dia, String horaE, String horaS,String correo, String num,
			String otros) {
		String dni = parsePaciente(paciente);
		LocalDate fecha = toFecha(a�o, mes , dia);
		ci.dniPaciente= dni;
		ci.idCita = String.valueOf(nextid);
		ci.correoPaciente = correo;
		ci.telefonoPaciente = num;
		ci.urgente = urgencia;
		ci.pacienteAcudido = false ;
		ci.lugar = lugar;
		ci.fecha = fecha;
		ci.horaEntradaEstimada = LocalTime.parse(horaE);
		ci.horaSalidaEstimada = LocalTime.parse(horaS);
		ci.otros = otros;
		CitaGatewayImpl cg = new CitaGatewayImpl();
		cg.add(ci);
	}
	
	private LocalDate toFecha(String a�o, String mes, String dia) {
		
		return LocalDate.parse(a�o+"-"+mes+"-"+dia);
	}

//	public void crearCita(String paciente, boolean urgencia,String lugar) {
//		String dni = parsePaciente(paciente);
//		List<String> contacto = sacarDatosContacto(dni);
//		String correo = contacto.get(0);
//		String num = contacto.get(1);
//		ci.dniPaciente= dni;
//		ci.idCita = String.valueOf(nextid);
//		ci.correoPaciente = correo;
//		ci.telefonoPaciente = num;
//		ci.urgente = urgencia;
//		ci.pacienteAcudido = false ;
//		ci.lugar = lugar;
//		CitaGatewayImpl cg = new CitaGatewayImpl();
//		cg.add(ci);
//	}
	public void crearCitaMedico(String med) {
		
		int idm = parseMed(med);
		MedicoGatewayImpl m = new MedicoGatewayImpl();
		MedicoCitaRecord mc = new MedicoCitaRecord();
		mc.idCita = String.valueOf(nextid);
		mc.idMedico = idm;
		MedicoCitaGatewayImpl i = new MedicoCitaGatewayImpl();
		i.add(mc);
		if (ci.urgente)
			enviarCorreo(idm);
		
	}
	
	private void enviarCorreo(int idm) {
		MedicoGatewayImpl m = new MedicoGatewayImpl();
		MedicoRecord medic=  m.findById(String.valueOf(idm)).get();
		String mensaje = "" + medic.nombre + " " + medic.apellidos + " Tiene usted una cita urgente en la "
				+ "" + ci.lugar + " con el paciete " + nombre;
		EnviarMail.enviaMail(medic.correo, mensaje);
	}
}
