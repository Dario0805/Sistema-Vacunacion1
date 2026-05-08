<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="templates/header.jsp">
    <jsp:param name="titulo" value="${accion == 'editar' ? 'Editar Horario' : 'Nuevo Horario'}" />
    <jsp:param name="menu" value="horarios" />
    <jsp:param name="icono" value="fas fa-clock" />
</jsp:include>

<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">Datos del horario</h6>
    </div>

    <div class="card-body">
        <form action="${pageContext.request.contextPath}/horarios" method="post">

            <c:if test="${not empty horario}">
                <input type="hidden" name="id" value="${horario.id}">
            </c:if>

            <div class="row">

                <div class="col-md-4 mb-3">
                    <label class="form-label">Día de la semana</label>
                    <select class="form-select" name="diaSemana" required>
                        <option value="">Seleccione</option>

                        <option value="LUNES" ${horario.diaSemana == 'LUNES' ? 'selected' : ''}>Lunes</option>
                        <option value="MARTES" ${horario.diaSemana == 'MARTES' ? 'selected' : ''}>Martes</option>
                        <option value="MIERCOLES" ${horario.diaSemana == 'MIERCOLES' ? 'selected' : ''}>Miércoles</option>
                        <option value="JUEVES" ${horario.diaSemana == 'JUEVES' ? 'selected' : ''}>Jueves</option>
                        <option value="VIERNES" ${horario.diaSemana == 'VIERNES' ? 'selected' : ''}>Viernes</option>
                        <option value="SABADO" ${horario.diaSemana == 'SABADO' ? 'selected' : ''}>Sábado</option>
                        <option value="DOMINGO" ${horario.diaSemana == 'DOMINGO' ? 'selected' : ''}>Domingo</option>
                    </select>
                </div>

                <div class="col-md-4 mb-3">
                    <label class="form-label">Hora inicio</label>
                    <input type="time" 
                           class="form-control" 
                           name="horaInicio" 
                           value="${horario.horaInicio}" 
                           required>
                </div>

                <div class="col-md-4 mb-3">
                    <label class="form-label">Hora fin</label>
                    <input type="time" 
                           class="form-control" 
                           name="horaFin" 
                           value="${horario.horaFin}" 
                           required>
                </div>

                <div class="col-md-6 mb-3">
                    <label class="form-label">Médico / Usuario</label>
                    <select class="form-select" name="idUsuario" required>
                        <option value="">Seleccione</option>

                        <c:forEach var="u" items="${usuarios}">
                            <option value="${u.id}" ${horario.idUsuario == u.id ? 'selected' : ''}>
                                ${u.nombres} ${u.apellidos} - ${u.especialidad}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="col-md-6 mb-3">
                    <label class="form-label">Estado</label>
                    <select class="form-select" name="estado" required>
                        <option value="DISPONIBLE" ${horario.estado == 'DISPONIBLE' ? 'selected' : ''}>
                            DISPONIBLE
                        </option>

                        <option value="OCUPADO" ${horario.estado == 'OCUPADO' ? 'selected' : ''}>
                            OCUPADO
                        </option>

                        <option value="INACTIVO" ${horario.estado == 'INACTIVO' ? 'selected' : ''}>
                            INACTIVO
                        </option>
                    </select>
                </div>

            </div>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Guardar
                </button>

                <a href="${pageContext.request.contextPath}/horarios" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Volver
                </a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="templates/footer.jsp" />