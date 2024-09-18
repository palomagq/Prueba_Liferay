<%@ include file="/init.jsp" %>

<%@ page import="com.virtualclassroom.paginalistadousuario.portlet.ListadoUsuarioPortlet" %>



<!-- <p>
	<b><liferay-ui:message key="listadousuario.caption"/></b>
</p>-->

<%@ page import="java.util.List" %>
<%@ page import="com.liferay.portal.kernel.json.JSONObject" %>
<portlet:defineObjects />

<%
    // Obtener los atributos desde el request
    List<JSONObject> usuarios = (List<JSONObject>) request.getAttribute("usuarios");
    int totalPages = (Integer) request.getAttribute("totalPages");
    int currentPage = (Integer) request.getAttribute("currentPage");
    String searchName = (String) request.getAttribute("searchName");
    String searchSurname = (String) request.getAttribute("searchSurname");
    String searchEmail = (String) request.getAttribute("searchEmail");
%>

<!-- Estilos en línea para mejorar la apariencia visual -->
<style>
    .search-form {
        margin-bottom: 20px;
    }

    .search-form input {
        margin-right: 10px;
        padding: 5px;
        font-size: 1em;
    }

    .search-form button {
        padding: 5px 10px;
        font-size: 1em;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
    }

    .search-form button:hover {
        background-color: #0056b3;
    }

    .users-table {
        width: 100%;
        border-collapse: collapse;
        margin-bottom: 20px;
    }

    .users-table th, .users-table td {
        padding: 10px;
        border: 1px solid #ddd;
        text-align: left;
    }

    .users-table th {
        background-color: #f2f2f2;
    }

    /*.pagination {
        display: flex;
        /*justify-content: center;
        margin-top: 20px;
    }

    .pagination a {
        margin: 0 5px;
        padding: 8px 16px;
        text-decoration: none;
        background-color: #007bff;
        color: white;
        border-radius: 5px;
    }

    .pagination .current-page{
        font-weight: bold;
        background-color: #0056b3;
    }
    
    .pagination .disabled {
        margin: 0 5px;
        color: #ccc;
        cursor: not-allowed;
    }*/
    
    .pagination a {
       	margin: 0 5px;
        padding: 8px 16px;
        text-decoration: none;
        background-color: #007bff;
        color: white;
        border-radius: 5px;
    }
    .pagination .current-page {
        font-weight: bold;
        background-color: #0056b3;
    }
    .pagination .disabled {
        margin: 0 5px;
        padding: 8px 16px;
        text-decoration: none;
        background-color: #cdcdcd;
        color: white;
        border-radius: 5px;
        pointer-events: none; /* Hace que el botón no sea clickeable */
    }
    /*.pagination a:hover {
        background-color: #ddd;
    }*/
</style>

<!-- Formulario de búsqueda -->
<div class="search-form">
    <form method="post" action="<portlet:renderURL/>">
        <input type="text" name="searchName" placeholder="Nombre" value="${searchName}">
        <input type="text" name="searchSurname" placeholder="Apellido" value="${searchSurname}">
        <input type="text" name="searchEmail" placeholder="Correo electrónico" value="${searchEmail}">
        <button type="submit">Buscar</button>
    </form>
</div>


<!-- Tabla de usuarios -->
<table class="users-table">
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Primer Apellido</th>
            <th>Segundo Apellido</th>
            <th>Email</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="usuario" items="${usuarios}">
            <tr>
                <td><c:out value="${usuario.getString('name')}"/></td>
                <td><c:out value="${usuario.getString('surname1')}"/></td>
                <td><c:out value="${usuario.getString('surname2')}"/></td>
                <td><c:out value="${usuario.getString('email')}"/></td>
            </tr>
        </c:forEach>
    </tbody>
</table>



<!-- Paginación -->
<div class="pagination">
      <!-- Botón Anterior -->
    <c:if test="${currentPage > 1}">
        <a href="?currentPage=${currentPage - 1}&searchName=${searchName}&searchSurname=${searchSurname}&searchEmail=${searchEmail}">Anterior</a>
    </c:if>
    <c:if test="${currentPage == 1}">
        <span class="btn disabled">Anterior</span>
    </c:if>
    
    
    <c:forEach begin="1" end="${totalPages}" var="i">
        <a href="?currentPage=${i}&searchName=${searchName}&searchSurname=${searchSurname}&searchEmail=${searchEmail}" class="${i == currentPage ? 'current-page' : ''}">
            ${i}
        </a>
    </c:forEach>
    
    <!-- Botón Siguiente -->
    <c:if test="${currentPage < totalPages}">
        <a href="?currentPage=${currentPage + 1}&searchName=${searchName}&searchSurname=${searchSurname}&searchEmail=${searchEmail}">Siguiente</a>
    </c:if>
    <c:if test="${currentPage == totalPages}">
        <span class="btn disabled">Siguiente</span>
    </c:if>
 </div>
<!--  
    <c:if test="${currentPage > 1}">
        <a href="?currentPage=${currentPage - 1}" class="btn">Anterior</a>
    </c:if>
    <c:if test="${currentPage == 1}">
        <span class="btn disabled">Anterior</span>
    </c:if>

    <c:forEach begin="1" end="${totalPages}" var="i">
        <a href="?currentPage=${i}" 
           class="btn <c:if test='${i == currentPage}'>current-page</c:if>">
            ${i}
        </a>
    </c:forEach>

    <c:if test="${currentPage < totalPages}">
        <a href="?currentPage=${currentPage + 1}" class="btn">Siguiente</a>
    </c:if>
    <c:if test="${currentPage == totalPages}">
        <span class="btn disabled">Siguiente</span>
    </c:if>
-->



