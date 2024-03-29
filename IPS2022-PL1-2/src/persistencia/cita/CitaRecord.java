package persistencia.cita;

import java.time.LocalDate;
import java.time.LocalTime;

import business.BusinessFactory;
import persistencia.paciente.PacienteRecord;
import util.BusinessException;

public class CitaRecord {

    public String idCita;
    public int idPaciente;
    public int idHistorial;
    public boolean urgente;
    public LocalTime horaEntradaEstimada;
    public LocalTime horaSalidaEstimada;
    public String pacienteAcudido;
    public LocalTime horaEntradaReal;
    public LocalTime horaSalidaReal;
    public LocalDate fecha;
    public String correoPaciente;
    public String telefonoPaciente;
    public String lugar;
    public String otros;
    public boolean prioritario;
    public PacienteRecord pacienteAsociado;
    public boolean confirmada;

    public PacienteRecord getPacienteAsociado() {
	try {
	    if (pacienteAsociado != null)
		return pacienteAsociado;
	    return BusinessFactory.forPacienteService().getById(idPaciente)
		    .get();
	} catch (BusinessException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public String toString() {
	PacienteRecord pacienteAsociado = getPacienteAsociado();
	return pacienteAsociado.getNombre() + " "
		+ pacienteAsociado.getApellidos() + " "
		+ horaEntradaEstimada.toString() + " - "
		+ horaSalidaEstimada.toString();
    }
}