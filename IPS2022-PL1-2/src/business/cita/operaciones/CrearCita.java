package business.cita.operaciones;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import business.BusinessFactory;
import persistencia.cita.CitaRecord;
import persistencia.cita.MedicoCitaRecord;
import persistencia.cita.impl.CitaGatewayImpl;
import persistencia.cita.impl.MedicoCitaGatewayImpl;
import persistencia.enfermero.EnfermeroCitaRecord;
import persistencia.enfermero.impl.EnfermeroCitaGatewayImpl;
import persistencia.especialidad.EspecialidadCitaRecord;
import persistencia.especialidad.impl.EspecialidadCitaGatewayImpl;
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
	int lastId = c.getLastId("CITA", "IDCITA");
	return lastId + 1;
    }

   
    private int parseMed(String s) {
	int idm = -1;
	String[] partes = s.split(" ");
	idm = Integer.parseInt(partes[partes.length - 1]);
	return idm;
    }

    public PacienteRecord sacarDatosContacto(String dni) {
	PacienteGatewayImpl pa = new PacienteGatewayImpl();
	PacienteRecord p = pa.findById(dni).get();

	return p;
    }

    public void crearCita(PacienteRecord paciente, boolean urgencia, String lugar,
	    String anio, String mes, String dia, String horaE, String horaS,
	    String correo, String num, String otros, boolean prio) {

	LocalDate fecha = toFecha(anio, mes, dia);
	PacienteGatewayImpl pa = new PacienteGatewayImpl();
	ci.idPaciente = paciente.getId();
	ci.idHistorial = pa.getHistorial(paciente.getId()).getIdHistorial();
	ci.idCita = String.valueOf(nextid);
	ci.correoPaciente = correo;
	ci.telefonoPaciente = num;
	ci.urgente = urgencia;
	ci.pacienteAcudido = "Asistencia Sin Asignar";
	ci.lugar = lugar;
	ci.fecha = fecha;
	ci.horaEntradaEstimada = LocalTime.parse(horaE);
	ci.horaSalidaEstimada = LocalTime.parse(horaS);
	ci.otros = otros;
	ci.prioritario = prio;
	ci.confirmada = true;
	almacena();

    }

    public void SolicitarCita(PacienteRecord paciente, boolean urgencia, String lugar,
	    String anio, String mes, String dia, String horaE, String horaS,
	    String correo, String num, boolean prio) {
	LocalDate fecha = toFecha(anio, mes, dia);
	PacienteGatewayImpl pa = new PacienteGatewayImpl();
	ci.idPaciente = paciente.getId();
	ci.idCita = String.valueOf(nextid);
	ci.correoPaciente = correo;
	ci.idHistorial = pa.getHistorial(paciente.getId()).getIdHistorial();
	ci.telefonoPaciente = num;
	ci.urgente = urgencia;
	ci.pacienteAcudido = "Asistencia Sin Asignar";
	ci.lugar = lugar;
	ci.fecha = fecha;
	ci.horaEntradaEstimada = LocalTime.parse(horaE);
	ci.horaSalidaEstimada = LocalTime.parse(horaS);
	ci.prioritario = prio;
	ci.confirmada = false;

	almacena();

    }

    public boolean comprovarChoqueCitas(String anio, String dia, String mes,
	    String hI, String hF) {
	CitaGatewayImpl cg = new CitaGatewayImpl();
	LocalTime inicio = LocalTime.parse(hI);
	LocalTime fin = LocalTime.parse(hF);

	try {
	    List<CitaRecord> citas = BusinessFactory.forCitaService()
		    .getCitasDelDia(Integer.valueOf(anio), Integer.valueOf(mes),
			    Integer.valueOf(dia));
	    if (citas.size() == 0)
		return false;
	    for (int i = 0; i < citas.size(); i++) {
		CitaRecord ci = citas.get(i);
		LocalTime hi = ci.horaEntradaEstimada;
		LocalTime hf = ci.horaSalidaEstimada;
		if (hi.compareTo(fin) > 0) {

		} else if (hf.compareTo(inicio) < 0) {

		} else {
		    return true;
		}

	    }

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return false;
    }

    public List<CitaRecord> citasChoque(String anio, String dia, String mes,
	    String hI, String hF) {
	List<CitaRecord> lista = new ArrayList<>();
	CitaGatewayImpl cg = new CitaGatewayImpl();
	LocalTime inicio = LocalTime.parse(hI);
	LocalTime fin = LocalTime.parse(hF);
	try {
	    List<CitaRecord> citas = BusinessFactory.forCitaService()
		    .getCitasDelDia(Integer.valueOf(anio), Integer.valueOf(mes),
			    Integer.valueOf(dia));
	    for (int i = 0; i < citas.size(); i++) {
		CitaRecord ci = citas.get(i);
		LocalTime hi = ci.horaEntradaEstimada;
		LocalTime hf = ci.horaSalidaEstimada;
		if (hi.compareTo(fin) > 0 || hf.compareTo(inicio) < 0
			|| ci.prioritario) {

		} else {
		    lista.add(ci);
		}
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return lista;
    }

    private void almacena() {
	CitaGatewayImpl cg = new CitaGatewayImpl();
	cg.add(ci);
    }

    private LocalDate toFecha(String anio, String mes, String dia) {

	return LocalDate.parse(anio + "-" + mes + "-" + dia);
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

    public void crearCitaEspecialidad(EspecialidadCitaRecord es) {
	EspecialidadCitaGatewayImpl ec = new EspecialidadCitaGatewayImpl();
	es.idCita = String.valueOf(nextid);
	ec.add(es);
    }

    private void enviarCorreo(int idm) {
	MedicoGatewayImpl m = new MedicoGatewayImpl();
	MedicoRecord medic = m.findById(String.valueOf(idm)).get();
	String mensaje = "" + medic.nombre + " " + medic.apellidos
		+ " Tiene usted una cita urgente en la " + "" + ci.lugar
		+ " con el paciete " + nombre;
	EnviarMail.enviaMail(medic.correo, mensaje);
    }

    public void crearCitaEnfermero(EnfermeroCitaRecord enfermeroCitaRecord) {
	EnfermeroCitaGatewayImpl ec = new EnfermeroCitaGatewayImpl();
	enfermeroCitaRecord.idCita = String.valueOf(nextid);
	ec.add(enfermeroCitaRecord);
    }
}