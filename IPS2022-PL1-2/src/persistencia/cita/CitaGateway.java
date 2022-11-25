package persistencia.cita;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import persistencia.Gateway;

public interface CitaGateway extends Gateway<CitaRecord> {
	
	void modificarHorario(String idCita,LocalTime nHoraEntrada ,LocalTime nHoraSalida);

    void asignarHoraEntrada(String idCita, LocalTime horaEntrada);

    void asignarHoraSalida(String idCita, LocalTime horaSalida);

    void setPacienteAcudido(String idCita, String estadoAsistencia);

    List<CitaRecord> getCitasDelDia(Date dia);

    List<CitaRecord> getCitasProximas(Date dia);

    void modificarCita(String idCita, String correo, int telefono);
    
    void insertarCausa(String idCita, String titulo, Time hora, Date fecha);
    
    List<CausaRecord> getCausas(String idCita);
    
    void removeCausa(int idCausa);

	List<PrescripcionRecord> getPrescripciones(String idCita);
	
	int getLastId(String tabla, String columnName);

	void insertarPrescripcion(PrescripcionRecord presc);

	void deletePrescripcion(PrescripcionRecord presc);

	List<CitaRecord> findByHistorialId(int idHistorial);
}
