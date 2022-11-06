package business.admin;

import java.util.List;

import persistencia.admin.JornadaComunRecord;
import persistencia.admin.JornadaRecord;
import persistencia.admin.MedicoRecord;
import util.BusinessException;

public interface AdminService {
    void asignarJornadasLaboralesMedicos(JornadaRecord jornada)
	    throws BusinessException;

    List<MedicoRecord> buscarMedicos() throws BusinessException;

    void crearJornadasLaboralesComunes(JornadaComunRecord jornada)
	    throws BusinessException;

    void asignarJornadasLaboralesComunes(JornadaComunRecord comun,
	    JornadaRecord jornada, MedicoRecord medico)
	    throws BusinessException;
}
