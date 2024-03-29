package persistencia.paciente;

import java.util.ArrayList;
import java.util.List;

import persistencia.PersistenceFactory;
import persistencia.cita.CausaRecord;
import persistencia.cita.CitaRecord;
import persistencia.cita.DiagnosticoRecord;
import persistencia.cita.PrescripcionRecord;

public class HistorialRecord {

	private int idHistorial;
  private int idPaciente;
	private List<CitaRecord> citas;
	private List<CausaRecord> causas;
	private List<PrescripcionRecord> prescripciones;
	private List<DiagnosticoRecord> diagnosticos;
	private List<VacunaRecord> vacunasRealizadas;
	private List<VacunaRecord> calendarioVacunas;
	
	public int getIdHistorial() {
		return idHistorial;
	}

    public void setIdHistorial(int idHistorial) {
	this.idHistorial = idHistorial;
    }

    public int getIDPaciente() {
	return idPaciente;
    }

    public void setIDPaciente(int idPaciente) {
	this.idPaciente = idPaciente;
    }

    public List<CitaRecord> getCitas() {
	if (citas == null) {
	    citas = PersistenceFactory.forCita().findByHistorialId(idHistorial);
	}
	return citas;
    }

    public List<CausaRecord> getCausas() {
	if (causas == null) {
	    List<CausaRecord> myCausas = new ArrayList<>();
	    for (CitaRecord cita : getCitas()) {
		myCausas.addAll(
			PersistenceFactory.forCita().getCausas(cita.idCita));
	    }
	    causas = myCausas;
	}
	return causas;
    }

	public List<DiagnosticoRecord> getDiagnosticos(){
		if (diagnosticos==null) {
			List<DiagnosticoRecord> myDiag = new ArrayList<>();
			for (CitaRecord cita : getCitas()) {
				myDiag.addAll(PersistenceFactory.forCita().getDiagnosticos(cita.idCita));
			}
			diagnosticos=myDiag;
		}
		return diagnosticos;
	}
	
	public List<PrescripcionRecord> getPrescripciones() {
		if (prescripciones==null) {
			List<PrescripcionRecord> myPrescripciones = new ArrayList<>();
			for (CitaRecord cita : getCitas()) {
				myPrescripciones.addAll(PersistenceFactory.forCita().getPrescripciones(cita.idCita));
			}
			prescripciones = myPrescripciones;
		}
		return prescripciones;
	}

    public List<VacunaRecord> getVacunasRealizadas() {
	if (vacunasRealizadas == null) {
	    List<VacunaRecord> allVacunas = PersistenceFactory.forPaciente()
		    .getVacunas(idHistorial);
	    List<VacunaRecord> myVacunasRealizadas = new ArrayList<>();
	    for (VacunaRecord vacuna : allVacunas) {
		if (vacuna.getFechaReal() != null && vacuna.getHora() != null)
		    myVacunasRealizadas.add(vacuna);
	    }
	    vacunasRealizadas = myVacunasRealizadas;
	}
	return vacunasRealizadas;
    }

    public List<VacunaRecord> getCalendarioVacunas() {
	if (calendarioVacunas == null) {
	    List<VacunaRecord> allVacunas = PersistenceFactory.forPaciente()
		    .getVacunas(idHistorial);
	    List<VacunaRecord> myCalendarioVacunas = new ArrayList<>();
	    for (VacunaRecord vacuna : allVacunas) {
		if (vacuna.getFechaReal() == null || vacuna.getHora() == null)
		    myCalendarioVacunas.add(vacuna);
	    }
	    calendarioVacunas = myCalendarioVacunas;
	}
	return calendarioVacunas;
    }
}
