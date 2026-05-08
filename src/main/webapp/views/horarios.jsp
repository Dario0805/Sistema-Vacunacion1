<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="templates/header.jsp">
    <jsp:param name="titulo" value="Horarios" />
    <jsp:param name="menu" value="horarios" />
    <jsp:param name="icono" value="fas fa-clock" />
    <jsp:param name="accionNuevo" value="${pageContext.request.contextPath}/horarios?accion=nuevo" />
</jsp:include>

<div class="card shadow mb-4">
    <div class="card-header py-3 d-flex justify-content-between align-items-center">
        <h6 class="m-0 font-weight-bold text-primary">Horarios disponibles</h6>

        <c:if test="${sessionScope.usuarioRol != 'MEDICO' 
                  && sessionScope.usuarioRol != 'ADMIN' 
                  && sessionScope.usuarioRol != 'ADMINISTRADOR'}">
            <span class="badge bg-info">
                <i class="fas fa-eye"></i> Solo visualización
            </span>
        </c:if>
    </div>

    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover datatable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Día</th>
                        <th>Inicio</th>
                        <th>Fin</th>
                        <th>Médico</th>
                        <th>Especialidad</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="horario" items="${horarios}">
                        <tr>
                            <td>${horario.id}</td>
                            <td>${horario.diaSemana}</td>
                            <td>${horario.horaInicio}</td>
                            <td>${horario.horaFin}</td>
                            <td>${horario.medico}</td>
                            <td>${horario.especialidad}</td>

                            <td>
                                <span class="badge ${horario.estado == 'DISPONIBLE' ? 'bg-success' : horario.estado == 'OCUPADO' ? 'bg-warning' : 'bg-secondary'}">
                                    ${horario.estado}
                                </span>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${sessionScope.usuarioRol == 'MEDICO' 
                                                || sessionScope.usuarioRol == 'ADMIN' 
                                                || sessionScope.usuarioRol == 'ADMINISTRADOR'}">

                                        <a href="${pageContext.request.contextPath}/horarios?accion=editar&id=${horario.id}" 
                                           class="btn btn-warning btn-sm" 
                                           title="Editar">
                                            <i class="fas fa-edit"></i>
                                        </a>

                                        <a href="#" 
                                           class="btn btn-danger btn-sm" 
                                           title="Eliminar" 
                                           onclick="confirmarEliminar(${horario.id}, '${horario.diaSemana} ${horario.horaInicio}')">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="badge bg-info">
                                            <i class="fas fa-eye"></i> Solo visualizar
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty horarios}">
                        <tr>
                            <td colspan="8" class="text-center text-muted">
                                No hay horarios registrados.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="eliminarModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    <i class="fas fa-trash"></i> Confirmar eliminación
                </h5>

                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <div class="modal-body">
                <p>¿Desea eliminar el horario <strong id="nombreHorario"></strong>?</p>
                <p class="text-danger">Esta acción no se puede deshacer.</p>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                    Cancelar
                </button>

                <a href="#" id="btnConfirmarEliminar" class="btn btn-danger">
                    Eliminar
                </a>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmarEliminar(id, nombre) {
        document.getElementById('nombreHorario').textContent = nombre;
        document.getElementById('btnConfirmarEliminar').href =
            '${pageContext.request.contextPath}/horarios?accion=eliminar&id=' + id;

        new bootstrap.Modal(document.getElementById('eliminarModal')).show();
    }
</script>

<jsp:include page="templates/footer.jsp" />