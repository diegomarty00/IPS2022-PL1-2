package persistencia.admin.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import persistencia.JornadaAssembler;
import persistencia.PersistenceException;
import persistencia.admin.AdminGateway;
import persistencia.admin.JornadaComunRecord;
import persistencia.admin.JornadaRecord;
import persistencia.admin.MedicoRecord;
import util.jdbc.Jdbc;

public class AdminGatewayImpl implements AdminGateway {

    private static final String ALL_MEDICOS = "SELECT * from MEDICO";
    private static final String ANIADIR_JORNADAS = "insert into JORNADA values (?, ?, ?, ?, ?)";
    private static final String CONTAR_JORNADAS = "SELECT count(*) from JORNADA";
    private static final String ANIADIR_JORNADASCOMUNES = "insert into JornadaComun values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String CONTAR_JORNADASCOMUNES = "SELECT count(*) from JornadaComun";
    private static final String FINDBYMEDICOS = "SELECT * from JORNADA where idmedico = ?";
    private static final String ASIGNAR_MEDICO_CABECERA_DNI = "UPDATE PACIENTE SET idmedicocabecera = ? where dni = ?";
    private static final String ASIGNAR_MEDICO_CABECERA_TUTOR = "UPDATE PACIENTE SET idmedicocabecera = ? where dnitutorlegal = ?";

    @Override
    public void add(MedicoRecord t) {
	// TODO Auto-generated method stub

    }

    @Override
    public void remove(String id) {
	// TODO Auto-generated method stub

    }

    @Override
    public void update(MedicoRecord t) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<MedicoRecord> findById(String id) {
	return null;

    }

    @Override
    public List<MedicoRecord> findAll() {
	Connection c = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	ArrayList<MedicoRecord> medicos = new ArrayList<>();
	try {
	    c = Jdbc.getCurrentConnection();

	    pst = c.prepareStatement(ALL_MEDICOS);

	    rs = pst.executeQuery();
	    while (rs.next()) {
		MedicoRecord medico = new MedicoRecord();
		medico.nombre = rs.getString("nombre");
		medico.apellidos = rs.getString("apellidos");
		medico.id = rs.getInt("idmedico");
		medico.correo = rs.getString("correo");
		medicos.add(medico);
	    }
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	} finally {
	    Jdbc.close(rs, pst);
	}
	return medicos;
    }

    @Override
    public void aniadirJornadas(JornadaRecord jornada) {

	Connection c = null;
	PreparedStatement pst = null;
	PreparedStatement pst_count = null;
	ResultSet rs = null;
	try {
	    System.out.println(jornada);
	    c = Jdbc.getCurrentConnection();
	    pst_count = c.prepareStatement(CONTAR_JORNADAS);

	    rs = pst_count.executeQuery();
	    pst = c.prepareStatement(ANIADIR_JORNADAS);
	    rs.next();
	    pst.setInt(1, rs.getInt(1));
	    pst.setInt(2, jornada.idMedico);
	    pst.setString(3, jornada.dia);
	    pst.setTimestamp(4, jornada.inicio);
	    pst.setTimestamp(5, jornada.fin);
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	} finally {
	    Jdbc.close(pst);
	    Jdbc.close(rs, pst_count);
	}

    }

    @Override
    public void asignarInformacionContactoCitas() {
	// TODO Auto-generated method stub

    }

    @Override
    public void crearJornadas(JornadaComunRecord jornada) {

	Connection c = null;
	PreparedStatement pst = null;
	PreparedStatement pst_count = null;
	ResultSet rs = null;
	String resultado = "";
	try {
	    System.out.println(jornada);
	    c = Jdbc.getCurrentConnection();
	    pst_count = c.prepareStatement(CONTAR_JORNADASCOMUNES);

	    rs = pst_count.executeQuery();

	    pst = c.prepareStatement(ANIADIR_JORNADASCOMUNES);

	    rs.next();
	    pst.setInt(1, rs.getInt(1));
	    pst.setString(2, jornada.nombre);
	    pst.setString(3, jornada.listado(resultado, jornada.lunes));
	    pst.setString(4, jornada.listado(resultado, jornada.martes));
	    pst.setString(5, jornada.listado(resultado, jornada.miercoles));
	    pst.setString(6, jornada.listado(resultado, jornada.jueves));
	    pst.setString(7, jornada.listado(resultado, jornada.viernes));
	    pst.setString(8, jornada.listado(resultado, jornada.sabado));
	    pst.setString(9, jornada.listado(resultado, jornada.domingo));
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	} finally {
	    Jdbc.close(pst);
	    Jdbc.close(rs, pst_count);
	}
    }

    @Override
    public List<JornadaRecord> findByMedico(String idMedico) {
	Connection c = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	List<JornadaRecord> jornadas = new ArrayList<>();
	try {
	    c = Jdbc.getCurrentConnection();

	    pst = c.prepareStatement(FINDBYMEDICOS);
	    pst.setString(1, idMedico);
	    rs = pst.executeQuery();

	    jornadas = JornadaAssembler.toJornadaList(rs);
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	} finally {
	    Jdbc.close(rs, pst);
	}
	return jornadas;
    }

    @Override
    public void asignarMedicoCabeceraDni(String dniPaciente, int idMedico) {
	Connection c = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	try {
	    c = Jdbc.getCurrentConnection();
	    pst = c.prepareStatement(ASIGNAR_MEDICO_CABECERA_DNI);
	    pst.setString(2, dniPaciente);
	    pst.setInt(1, idMedico);
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	} finally {
	    Jdbc.close(rs, pst);
	}
    }

    @Override
    public void asignarMedicoCabeceraTutor(String dniTutor, int idMedico) {
	Connection c = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	try {
	    c = Jdbc.getCurrentConnection();
	    pst = c.prepareStatement(ASIGNAR_MEDICO_CABECERA_TUTOR);
	    pst.setString(2, dniTutor);
	    pst.setInt(1, idMedico);
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	} finally {
	    Jdbc.close(rs, pst);
	}
    }
}
