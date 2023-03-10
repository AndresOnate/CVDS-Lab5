package edu.eci.cvds.servlet;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;

import edu.eci.cvds.servlet.model.Todo;

@WebServlet(
    urlPatterns = "/olayaOñate"
)
public class Servlet extends HttpServlet{

    static final long serialVersionUID = 35L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Writer responseWriter = resp.getWriter();

        try{
            // El id es un entero, Si se le paso un valor al parametro las banderas seran true, se optione con el metodo get de la clase optional
            Optional<String> optId = Optional.ofNullable(req.getParameter("Id")); //Objecto Opcional del parametro Id de la solicitud
            // Si no se paso un valor en el paramtro se le asigna 0
            int id = optId.isPresent() && !optId.get().isEmpty() ? Integer.parseInt(optId.get()) : 0; 
            // Consultando item con la clase Service
            Todo item = Service.getTodo(id);
            // Respondiendo con el código HTTP que equivale a ‘OK’
            resp.setStatus(HttpServletResponse.SC_OK);
            // Tabla
            ArrayList<Todo> todoList = new ArrayList<>();
            todoList.add(item);
            responseWriter.write(Service.todosToHTMLTable(todoList));
            responseWriter.flush();
        }catch(NotFoundException e){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);  
        }catch(NumberFormatException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }catch(MalformedURLException e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }catch(Exception e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}