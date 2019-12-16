
package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DAO.*;
import entities.*;
import java.util.List;

public class UIController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String choose = request.getParameter("choose");
        
        switch(choose){
            case "showAll":
        OwnerDAO ownerDAO = new OwnerDAO();
        List<Owner> owner = ownerDAO.getAll();

        PetDAO petDAO = new PetDAO();
        List<Pet> pet = petDAO.getAll();
        
        request.setAttribute("dataP", owner);  
        request.setAttribute("dataO", pet);
        request.getRequestDispatcher("lists.jsp").forward(request, response);
        break;
       
            case "create":
                request.getRequestDispatcher("create.jsp").forward(request, response);
                break;
        }     
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         String choose = request.getParameter("choose");
         
        switch (choose) {
            case "created":
                Owner owner1 = new Owner(request.getParameter("AccNumber"),
                        request.getParameter("Name"));

                OwnerDAO owner2DAO = new OwnerDAO();
                owner2DAO.create(owner1);
                break;
        }
         
        PetDAO petDAO = new PetDAO();
        List<Pet> pet = petDAO.getAll();

        OwnerDAO ownerDAO = new OwnerDAO();
        List<Owner> owner = ownerDAO.getAll();
        
        request.setAttribute("dataP", pet);  
        request.setAttribute("dataO", owner);
        request.getRequestDispatcher("lists.jsp").forward(request, response);
        }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
