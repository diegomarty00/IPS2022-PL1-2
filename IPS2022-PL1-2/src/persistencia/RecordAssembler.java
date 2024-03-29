package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import persistencia.admin.JornadaComunRecord;
import persistencia.cita.CausaRecord;
import persistencia.cita.CitaRecord;
import persistencia.cita.DiagnosticoRecord;
import persistencia.cita.MedicoCitaRecord;
import persistencia.cita.PrescripcionRecord;
import persistencia.enfermero.EnfermeroRecord;
import persistencia.medico.MedicoRecord;
import persistencia.paciente.HistorialRecord;
import persistencia.paciente.PacienteRecord;
import persistencia.paciente.VacunaRecord;

public class RecordAssembler {

    public static MedicoCitaRecord rsToMedicosCita(ResultSet r)
	    throws SQLException {
	MedicoCitaRecord mc = new MedicoCitaRecord();
	mc.idCita = r.getString("idcita");
	mc.idMedico = r.getInt("idmedico");

	return mc;
    }

    public static List<MedicoCitaRecord> toMedicoCitaList(ResultSet r)
	    throws SQLException {
	List<MedicoCitaRecord> list = new ArrayList<>();
	while (r.next()) {
	    list.add(rsToMedicosCita(r));
	}
	return list;
    }

    public static MedicoRecord rsToMedico(ResultSet r) throws SQLException {
	MedicoRecord med = new MedicoRecord();
	med.idMedico = r.getInt("idmedico");
	med.nombre = r.getString("nombre");
	med.apellidos = r.getString("apellidos");
	med.correo = r.getString("correo");

	return med;
    }

    private static VacunaRecord rsToVacuna(ResultSet rs) throws SQLException {
	int idVac = rs.getInt("IDVACUNA");
	int idHist = rs.getInt("IDHISTORIAL");
	String idCita = rs.getString("IDCITA");
	LocalDate fechaReal = (rs.getDate("FECHAREAL") == null ? null
		: rs.getDate("FECHAREAL").toLocalDate());
	LocalDate fechaAprox = (rs.getDate("FECHAAPROXIMADA") == null ? null
		: rs.getDate("FECHAAPROXIMADA").toLocalDate());
	LocalTime hora = (rs.getTime("HORA") == null ? null
		: rs.getTime("HORA").toLocalTime());
	String dosis = rs.getString("DOSIS");
	boolean refuerzo = rs.getBoolean("REFUERZO");
	return new VacunaRecord(idVac, idHist, idCita, fechaReal, fechaAprox,
		hora, dosis, refuerzo);
    }

    public static EnfermeroRecord rsToEnfermero(ResultSet r)
	    throws SQLException {
	EnfermeroRecord enf = new EnfermeroRecord();
	enf.idEnfermero = r.getInt("idenfermero");
	enf.nombre = r.getString("nombre");
	enf.apellidos = r.getString("apellidos");

	return enf;
    }

    public static Optional<EnfermeroRecord> rsToEnfermeroO(ResultSet r)
	    throws SQLException {
	if (r.next()) {
	    return Optional.of(rsToEnfermero(r));
	} else
	    return Optional.ofNullable(null);
    }

    public static Optional<MedicoRecord> rsToMedicoO(ResultSet r)
	    throws SQLException {
	if (r.next()) {
	    return Optional.of(rsToMedico(r));
	} else
	    return Optional.ofNullable(null);
    }

    public static List<MedicoRecord> toMedicoList(ResultSet r)
	    throws SQLException {
	List<MedicoRecord> list = new ArrayList<>();
	while (r.next()) {
	    list.add(rsToMedico(r));
	}
	return list;
    }

    public static List<PacienteRecord> toPacienteList(ResultSet r)
	    throws SQLException {
	List<PacienteRecord> list = new ArrayList<>();
	while (r.next()) {
	    list.add(resultSetToPacienteRecord(r));
	}
	return list;
    }

    private static CitaRecord resultSetToCitaRecord(ResultSet rs)
	    throws SQLException {
	CitaRecord cita = new CitaRecord();

	cita.idCita = rs.getString("IDCITA");
	cita.idPaciente = rs.getInt("IDPACIENTE");
	cita.idHistorial = rs.getInt("IDHISTORIAL");
	cita.urgente = rs.getBoolean("URGENTE");
	if (rs.getTime("HORA_ENTRADA_ESTIMADA") != null)
	    cita.horaEntradaEstimada = rs.getTime("HORA_ENTRADA_ESTIMADA")
		    .toLocalTime();

	if (rs.getTime("HORA_SALIDA_ESTIMADA") != null)
	    cita.horaSalidaEstimada = rs.getTime("HORA_SALIDA_ESTIMADA")
		    .toLocalTime();

	cita.pacienteAcudido = rs.getString("PACIENTE_ACUDIDO");
	if (rs.getTime("HORA_ENTRADA_REAL") != null)
	    cita.horaEntradaReal = rs.getTime("HORA_ENTRADA_REAL")
		    .toLocalTime();
	if (rs.getTime("HORA_SALIDA_REAL") != null)
	    cita.horaSalidaReal = rs.getTime("HORA_SALIDA_REAL").toLocalTime();
	cita.fecha = rs.getDate("FECHA").toLocalDate();
	cita.correoPaciente = rs.getString("CORREO_PACIENTE");
	cita.telefonoPaciente = rs.getString("TELEFONO_PACIENTE");
	cita.lugar = rs.getString("LUGAR_CITA");
	cita.otros = rs.getString("OTROS");
	cita.prioritario = rs.getBoolean("PRIORITARIO");
	cita.confirmada = rs.getBoolean("CONFIRMADA");

	return cita;
    }

    private static PacienteRecord resultSetToPacienteRecord(ResultSet rs)
	    throws SQLException {
	PacienteRecord paciente = new PacienteRecord();
	paciente.setId(rs.getInt("id"));
	paciente.setDniPaciente(rs.getString("DNI"));
	paciente.setNombre(rs.getString("NOMBRE"));
	paciente.setApellidos(rs.getString("APELLIDOS"));
	paciente.setCorreo(rs.getString("CORREO"));
	paciente.setTelefono(rs.getInt("TELEFONO"));

	return paciente;
    }

    private static HistorialRecord resultSetToHistorialRecord(ResultSet rs)
	    throws SQLException {
	HistorialRecord historial = new HistorialRecord();
	historial.setIdHistorial(rs.getInt("IDHISTORIAL"));
	historial.setIDPaciente(rs.getInt("IDPACIENTE"));
	return historial;
    }

    private static CausaRecord resultSetToCausaRecord(ResultSet rs)
	    throws SQLException {
	return new CausaRecord(rs.getInt("IDCAUSA"), rs.getString("TITULO"),
		rs.getTime("HORA_ASIGNACION").toLocalTime(),
		rs.getDate("FECHA_ASIGNACION").toLocalDate(),
		rs.getString("IDCITA"));
    }

    private static PrescripcionRecord resultSetToPrescripcionRecord(
	    ResultSet rs) throws SQLException {
	if (rs.getString("TIPO").equalsIgnoreCase("MEDICAMENTO"))
	    return new PrescripcionRecord(rs.getInt("IDPRESCRIPCION"),
		    rs.getString("TITULO"), rs.getString("TIPO"),
		    rs.getString("CANTIDAD"), rs.getString("INTERVALO_DOSIS"),
		    rs.getString("DURACION"), rs.getString("OBSERVACIONES"),
		    rs.getTime("HORA_ASIGNACION").toLocalTime(),
		    rs.getDate("FECHA_ASIGNACION").toLocalDate(),
		    rs.getString("IDCITA"));
	else
	    return new PrescripcionRecord(rs.getInt("IDPRESCRIPCION"),
		    rs.getString("TITULO"), rs.getString("TIPO"),
		    rs.getString("OBSERVACIONES"),
		    rs.getTime("HORA_ASIGNACION").toLocalTime(),
		    rs.getDate("FECHA_ASIGNACION").toLocalDate(),
		    rs.getString("IDCITA"));
    }

    public static Optional<CitaRecord> toCitaRecord(ResultSet rs)
	    throws SQLException {
	if (rs.next()) {
	    return Optional.of(resultSetToCitaRecord(rs));
	} else
	    return Optional.ofNullable(null);
    }

    public static Optional<PacienteRecord> toPacienteRecord(ResultSet rs)
	    throws SQLException {
	if (rs.next()) {
	    return Optional.of(resultSetToPacienteRecord(rs));
	} else
	    return Optional.ofNullable(null);
    }

    public static List<CitaRecord> toCitaList(ResultSet rs)
	    throws SQLException {
	List<CitaRecord> res = new ArrayList<>();
	while (rs.next()) {
	    res.add(resultSetToCitaRecord(rs));
	}
	return res;
    }

    public static Optional<HistorialRecord> toHistorialRecord(ResultSet rs)
	    throws SQLException {
	if (rs.next()) {
	    return Optional.of(resultSetToHistorialRecord(rs));
	} else
	    return Optional.ofNullable(null);
    }

    public static List<HistorialRecord> toHistorialList(ResultSet rs)
	    throws SQLException {
	List<HistorialRecord> list = new ArrayList<>();
	while (rs.next()) {
	    list.add(resultSetToHistorialRecord(rs));
	}
	return list;
    }

    public static List<CausaRecord> toCausaList(ResultSet rs)
	    throws SQLException {
	List<CausaRecord> list = new ArrayList<CausaRecord>();
	while (rs.next())
	    list.add(resultSetToCausaRecord(rs));
	return list;
    }

    public static List<PrescripcionRecord> toPrescripcionList(ResultSet rs)
	    throws SQLException {
	List<PrescripcionRecord> list = new ArrayList<PrescripcionRecord>();
	while (rs.next())
	    list.add(resultSetToPrescripcionRecord(rs));
	return list;
    }

    public static List<EnfermeroRecord> toEnfermeroList(ResultSet rs)
	    throws SQLException {
	List<EnfermeroRecord> list = new ArrayList<>();
	while (rs.next()) {
	    list.add(rsToEnfermero(rs));
	}
	return list;
    }

    public static List<VacunaRecord> toVacunaList(ResultSet rs)
	    throws SQLException {
	List<VacunaRecord> vacunas = new ArrayList<>();
	while (rs.next()) {
	    vacunas.add(rsToVacuna(rs));
	}
	return vacunas;
    }

    public static Optional<JornadaComunRecord> toJornadaComunRecord(
	    ResultSet rs) throws SQLException {
	if (rs.next()) {
	    return Optional.of(resultSetToJornadaComunRecord(rs));
	} else
	    return Optional.ofNullable(null);
    }

    private static JornadaComunRecord resultSetToJornadaComunRecord(
	    ResultSet rs) throws SQLException {
	JornadaComunRecord jornada = new JornadaComunRecord();
	jornada.nombre = (rs.getString("nombre"));
	jornada.transformar(rs.getString("lunes"), "lunes");
	jornada.transformar(rs.getString("martes"), "martes");
	jornada.transformar(rs.getString("miercoles"), "miercoles");
	jornada.transformar(rs.getString("jueves"), "jueves");
	jornada.transformar(rs.getString("viernes"), "viernes");
	jornada.transformar(rs.getString("sabado"), "sabado");
	jornada.transformar(rs.getString("domingo"), "domingo ");
	return jornada;
    }

  	public static List<DiagnosticoRecord> toDiagnosticoList(ResultSet rs) throws SQLException {
		List<DiagnosticoRecord> diag = new ArrayList<>();
		while (rs.next()) {
			diag.add(toDiagnostico(rs));
		}
		return diag;
	}

	private static DiagnosticoRecord toDiagnostico(ResultSet rs) throws SQLException {
		DiagnosticoRecord diag = new DiagnosticoRecord(rs.getString("TITULO"));
		diag.setIdDiagnostico(rs.getInt("IDDIAGNOSTICO"));
		diag.setIdCita(rs.getString("IDCITA"));
		diag.setHoraAsginacion(rs.getTime("HORA").toLocalTime());
		diag.setFechaAsignacion(rs.getDate("FECHA").toLocalDate());
		diag.setMedicoAsociado(PersistenceFactory.forMedico().findById(String.valueOf(rs.getInt("IDMEDICO"))).get());
		return diag;
	}
  
    public static Optional<HistorialRecord> rsToHistorialO(ResultSet rs)
	    throws SQLException {
	if (rs.next()) {
	    return Optional.of(rsToHistorial(rs));
	} else
	    return Optional.ofNullable(null);
    }

    public static HistorialRecord rsToHistorial(ResultSet r)
	    throws SQLException {
	HistorialRecord historial = new HistorialRecord();
	historial.setIdHistorial(r.getInt("IDHISTORIAL"));
	historial.setIDPaciente(r.getInt("IDPACIENTE"));

	return historial;
    }
}
