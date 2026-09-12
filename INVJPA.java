// ---------- Step 2: MySQL Database ----------
/*
Run this in MySQL:
---------------------------------------------
USE Inventory;

CREATE TABLE inventory (
  itemid INT PRIMARY KEY AUTO_INCREMENT,
  itemName VARCHAR(50),
  description VARCHAR(200),
  quantity INT(40),
  addedDate VARCHAR(40)
);
---------------------------------------------
*/

// ---------- Step 4: Inventory.java ----------
package myApp;
import javax.persistence.*;

@Entity
@Table(name="Inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="itemid", unique=true, updatable=false)
    private Integer itemid;

    @Column(name="itemName")
    private String itemName;

    @Column(name="description")
    private String description;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="addedDate")
    private String addedDate;

    public Inventory() {}

    public Integer getItemID() { return itemid; }
    public void setItemID(Integer itemid) { this.itemid = itemid; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getAddedDate() { return addedDate; }
    public void setAddedDate(String addedDate) { this.addedDate = addedDate; }
}

// ---------- Step 5: persistence.xml ----------
/*
Place this inside META-INF/persistence.xml
---------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1"
 xmlns="http://xmlns.jcp.org/xml/ns/persistence"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
 http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">

  <persistence-unit name="INVJPAPU" transaction-type="RESOURCE_LOCAL">
    <non-jta-data-source>jdbc/Inventory</non-jta-data-source>
    <exclude-unlisted-classes>false</exclude-unlisted-classes>
    <properties/>
  </persistence-unit>
</persistence>
---------------------------------------------
*/

// ---------- Step 6: index.jsp ----------
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Inventory</title>
</head>
<body>
<form action="checkInventory.jsp" method="post">
<table>
<tr><td colspan="2" align="center"><b>Add the item in Inventory</b></td></tr>
<tr><td>Item Name:</td>
<td><input name="itemname" maxlength="25" size="50" /></td></tr>
<tr><td>Description:</td>
<td><textarea rows="5" cols="36" name="description"></textarea></td></tr>
<tr><td>Quantity:</td>
<td><input name="quantity" maxlength="4" size="5" /></td></tr>
<tr><td colspan="2">
<input type="submit" name="AddToInventory" value="AddToInventory" />
</td></tr>
</table>
</form>
</body>
</html>

// ---------- Step 7: checkInventory.jsp ----------
<%@page import="java.util.*,javax.persistence.*,myApp.Inventory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
private EntityManagerFactory eMF;
private EntityManager eM;
private EntityTransaction eT;
List<Inventory> inv;
%>
<%
eMF = Persistence.createEntityManagerFactory("INVJPAPU");
eM = eMF.createEntityManager();

String submit = request.getParameter("AddToInventory");
if(submit != null && ("AddToInventory").equals(submit)) {
    try {
        String itemname = request.getParameter("itemname");
        String description = request.getParameter("description");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String addedDate = new java.util.Date().toString();

        Inventory invObj = new Inventory();
        invObj.setItemName(itemname);
        invObj.setDescription(description);
        invObj.setQuantity(quantity);
        invObj.setAddedDate(addedDate);

        eT = eM.getTransaction();
        eT.begin();
        eM.persist(invObj);
        eT.commit();
    } catch (RuntimeException e) {
        if(eT != null) eT.rollback();
        throw e;
    }
    response.sendRedirect("checkInventory.jsp");
}

try {
    inv = eM.createQuery("SELECT i FROM Inventory i").getResultList();
} catch (RuntimeException e) {
    throw e;
}
eM.close();
%>
<html>
<head><title>Inventory</title></head>
<body>
<table border="1">
<tr><td colspan="5" align="center">
<b>Click <a href="index.jsp">here</a> to add items in the Inventory.</b>
</td></tr>
<tr><td>Item No</td><td>Item Name</td><td>Description</td><td>Quantity</td><td>Added Date & Time</td></tr>
<%
Iterator iterator = inv.iterator();
while (iterator.hasNext()) {
    Inventory objInv = (Inventory) iterator.next();
%>
<tr>
<td><b><%= objInv.getItemID()%></b></td>
<td><b><%= objInv.getItemName()%></b></td>
<td><b><%= objInv.getDescription()%></b></td>
<td><b><%= objInv.getQuantity()%></b></td>
<td><b><%= objInv.getAddedDate()%></b></td>
</tr>
<%
}
%>
</table>
</body>
</html>
