package com.virtualclassroom.paginalistadousuario.portlet;

import com.virtualclassroom.paginalistadousuario.constants.ListadoUsuarioPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author palomagq
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=ListadoDeUsuario",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + ListadoUsuarioPortletKeys.LISTADODEUSUARIO,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class ListadoUsuarioPortlet extends MVCPortlet {
	

	private static final int USERS_PER_PAGE = 5;

    // Simulación de API que devuelve un JSON con los usuarios
  /*  private JSONObject simularApiUsuarios() {
        JSONArray usuariosArray = JSONFactoryUtil.createJSONArray();
        for (int i = 1; i <= 22; i++) {
            JSONObject usuario = JSONFactoryUtil.createJSONObject();
            usuario.put("id", i);
            usuario.put("name", "admin" + i);
            usuario.put("surname1", "admin" + i);
            usuario.put("surname2", "admin" + i);
            usuario.put("email", "admin" + i + "@example.com");
            usuariosArray.put(usuario);
        }

        JSONObject apiResponse = JSONFactoryUtil.createJSONObject();
        apiResponse.put("usuarios", usuariosArray);
        return apiResponse;
    }

    // Obtener usuarios desde la simulación de la API
    private List<JSONObject> obtenerUsuariosDeApi() {
        // Simulación de llamada a una API externa
        JSONObject jsonObject = simularApiUsuarios(); // Obtener el JSONObject de la API simulada
        JSONArray jsonArray = jsonObject.getJSONArray("usuarios");

        List<JSONObject> usuarios = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            usuarios.add(jsonArray.getJSONObject(i));
        }
        return usuarios;

    }*/
    
	
	private String leerArchivoJSON(InputStream inputStream) throws IOException {
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
	        return reader.lines().collect(Collectors.joining("\n"));
	    }
	}
    
    // Método para leer el archivo JSON desde el classpath y obtener los usuarios
    private List<JSONObject> leerUsuariosDeArchivoJSON() throws IOException, JSONException {
    	 // Ruta del archivo JSON en el classpath
        String archivoJson = "/META-INF/resources/listadousuario.json";
        
        // Leer el archivo desde los recursos del portlet
        InputStream inputStream = getClass().getResourceAsStream(archivoJson);

        if (inputStream == null) {
            throw new IOException("No se pudo encontrar el archivo JSON: " + archivoJson);
        }

        // Leer el contenido del archivo JSON como String
        String jsonStr = leerArchivoJSON(inputStream);
        
        // Parsear el String como JSONObject
        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(jsonStr);
        JSONArray usuariosArray = jsonObject.getJSONArray("usuarios");
        
        List<JSONObject> usuarios = new ArrayList<>();
        for (int i = 0; i < usuariosArray.length(); i++) {
            usuarios.add(usuariosArray.getJSONObject(i));
        }
        
        return usuarios;
    }

    // Método para simular la API de usuarios usando el archivo JSON
    private List<JSONObject> obtenerUsuariosDeApi() throws IOException, JSONException {
        // Leer el archivo JSON y devolver los usuarios
        return leerUsuariosDeArchivoJSON();
    }
    
    // Filtro de usuarios basado en nombre, apellidos y correo electrónico
    public List<JSONObject> filtrarUsuarios(JSONArray usuarios, String name, String surname, String email) {
        List<JSONObject> usuariosFiltrados = new ArrayList<>();

        for (int i = 0; i < usuarios.length(); i++) {
            JSONObject usuario = usuarios.getJSONObject(i);
            if ((name.isEmpty() || usuario.getString("name").toLowerCase().contains(name.toLowerCase())) &&
                (surname.isEmpty() || usuario.getString("surname1").toLowerCase().contains(surname.toLowerCase())) &&
                (email.isEmpty() || usuario.getString("email").toLowerCase().contains(email.toLowerCase()))) {
                usuariosFiltrados.add(usuario);
            }
        }

        return usuariosFiltrados;
    }
    
 // Método para filtrar usuarios según los campos de búsqueda
   /* public List<JSONObject> filtrarUsuarios(JSONArray usuarios, String name, String surname, String email) {
        List<JSONObject> usuariosFiltrados = new ArrayList<>();

        for (int i = 0; i < usuarios.length(); i++) {
            JSONObject usuario = usuarios.getJSONObject(i);

            // Comprobación individual para cada campo. Solo se aplican si el campo no está vacío.
            boolean coincideNombre = name.isEmpty() || usuario.getString("name").toLowerCase().contains(name.toLowerCase());
            boolean coincideApellido = surname.isEmpty() || usuario.getString("surname1").toLowerCase().contains(surname.toLowerCase());
            boolean coincideEmail = email.isEmpty() || usuario.getString("email").toLowerCase().contains(email.toLowerCase());

            // Solo agregar el usuario si cumple con todos los filtros proporcionados
            if (coincideNombre && coincideApellido && coincideEmail) {
                usuariosFiltrados.add(usuario);
            }
        }

        return usuariosFiltrados;
    }*/
    
    
 /*   public void addEntry(ActionRequest request, ActionResponse response) {
        try {
            PortletPreferences prefs = request.getPreferences();

            String[] guestbookEntries = prefs.getValues("guestbook-entries",
                    new String[1]);

            ArrayList<String> entries = new ArrayList<String>();

            if (guestbookEntries[0] != null) {
                entries = new ArrayList<String>(Arrays.asList(prefs.getValues(
                        "guestbook-entries", new String[1])));
            }
            
            String searchName = ParamUtil.getString(request, "searchName", "");
            String searchSurname = ParamUtil.getString(request, "searchSurname", "");
            String searchEmail = ParamUtil.getString(request, "searchEmail", "");

          
            String entry = searchName + " " + searchSurname + " " + searchEmail;

            entries.add(entry);

            String[] array = entries.toArray(new String[entries.size()]);

            prefs.setValues("guestbook-entries", array);

            try {
                prefs.store();
            }
            catch (IOException ex) {
                Logger.getLogger(ListadoUsuarioPortlet.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
            catch (ValidatorException ex) {
                Logger.getLogger(ListadoUsuarioPortlet.class.getName()).log(
                        Level.SEVERE, null, ex);
            }

        }
        catch (ReadOnlyException ex) {
            Logger.getLogger(ListadoUsuarioPortlet.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }*/
    
    
    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
        throws IOException, PortletException {
    	
        // Obtener parámetros de búsqueda
        HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
        int currentPage = 1;
        //Integer.parseInt(httpReq.getParameter("currentPage"));
        String currentPageParam = httpReq.getParameter("currentPage");
        if(currentPageParam!=null && !currentPageParam.equals("")) {
        	currentPage = Integer.parseInt(currentPageParam);
        }
        final String searchName = (httpReq.getParameter("searchName") != null) ? httpReq.getParameter("searchName") : "";
        final String searchSurname = (httpReq.getParameter("searchSurname") != null) ? httpReq.getParameter("searchSurname") : "";
        final String searchEmail = (httpReq.getParameter("searchEmail") != null) ? httpReq.getParameter("searchEmail") : "";
        
        /*String searchName = ParamUtil.getString(renderRequest, "searchName", "");
        String searchSurname = ParamUtil.getString(renderRequest, "searchSurname", "");
        String searchEmail = ParamUtil.getString(renderRequest, "searchEmail", "");
        */
        
        String url =  PortalUtil.getCurrentURL(renderRequest);
        
        // Obtener usuarios desde la API simulada (archivo JSON)
        List<JSONObject> usuarios;
		try {
			usuarios = obtenerUsuariosDeApi(); // Usamos obtenerUsuariosDeApi()
	        // Filtrar los usuarios según los parámetros de búsqueda
	        List<JSONObject> usuariosFiltrados = usuarios.stream()
	            .filter(usuario -> (searchName.isEmpty() || usuario.getString("name").toLowerCase().contains(searchName.toLowerCase())) &&
	                               (searchSurname.isEmpty() || usuario.getString("surname1").toLowerCase().contains(searchSurname.toLowerCase())) &&
	                               (searchEmail.isEmpty() || usuario.getString("email").toLowerCase().contains(searchEmail.toLowerCase())))
	            .collect(Collectors.toList());

	        // Paginación
	        //int currentPage = ParamUtil.getInteger(renderRequest, "currentPage", 1);
	        int totalUsuarios = usuariosFiltrados.size();
	        int totalPages = (int) Math.ceil((double) totalUsuarios / USERS_PER_PAGE);
	        
	        System.out.println("Current Page: " + currentPage);
	        System.out.println("searchName: " + searchName);
	        System.out.println("searchSurname: " + searchSurname);
	        System.out.println("searchEmail: " + searchEmail);
	        System.out.println("url: " + url);


	        if (currentPage > totalPages) {
	            currentPage = totalPages;
	        }
	        if (currentPage < 1) {
	            currentPage = 1;
	        }


	        // Obtener la sublista paginada
	        int start = (currentPage - 1) * USERS_PER_PAGE;
	        int end = Math.min(start + USERS_PER_PAGE, totalUsuarios);
	        List<JSONObject> usuariosPaginados = usuariosFiltrados.subList(start, end);

	        // Pasar atributos a la vista
	        renderRequest.setAttribute("usuarios", usuariosPaginados);
	        renderRequest.setAttribute("totalPages", totalPages);
	        renderRequest.setAttribute("currentPage", currentPage);
	        renderRequest.setAttribute("searchName", searchName);
	        renderRequest.setAttribute("searchSurname", searchSurname);
	        renderRequest.setAttribute("searchEmail", searchEmail);
	        
	        super.doView(renderRequest, renderResponse);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

    }
	
}