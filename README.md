# Recipe Management & Cooking Documentation System (RMCDS)  

**A complete Recipe Management System** with JWT Auth, RBAC, File Upload, Likes, Comments, Versioning, Audit Logs & Full Search.

**GitHub Repository**: https://github.com/ShreyaCoditas/L-3-Problem-Statement  
**Submitted by**: Shreya  

---

### Features Implemented (As Per Problem Statement)

- JWT Authentication + Role-Based Access Control (Admin, Chef, User)
- Recipe CRUD with secure file upload (PDF, DOCX, JPG, PNG – max 10MB)
- Recipe categorization, versioning & soft delete
- Full-text search + filters (title, ingredients, category, difficulty, duration)
- Users can Like and Comment on recipes
- Complete audit logging of all actions
- Pagination & proper validation

---

### All API Endpoints (Complete List)

| Method | Endpoint                                      | Allowed Roles         | Description                          |
|--------|-----------------------------------------------|-----------------------|--------------------------------------|
| POST   | `/auth/register`                              | Public                | Register new user                    |
| POST   | `/auth/login`                                 | Public                | Login → returns JWT token            |
| POST   | `/api/category/add`                           | ADMIN, CHEF           | Add new category                     |
| PUT    | `/api/category/update/{categoryId}`           | ADMIN, CHEF           | Update category                      |
| POST   | `/api/recipes/add`                            | ADMIN, CHEF           | Add new recipe + file upload         |
| PUT    | `/api/recipes/update/{recipeId}`              | ADMIN, CHEF           | Update recipe                        |
| DELETE | `/api/recipes/delete/{recipeId}`              | ADMIN, CHEF           | Soft delete recipe                   |
| GET    | `/api/recipes/all`                            | USER, ADMIN           | Get all recipes (paginated)          |
| GET    | `/api/recipes/home`                           | USER, ADMIN           | Homepage feed                        |
| GET    | `/api/recipes/search`                         | USER, ADMIN           | Search recipes (with query params)   |
| POST   | `/api/recipes/likes/{recipeId}`               | USER, ADMIN           | Like a recipe                        |
| POST   | `/api/recipes/unlikes/{recipeId}`             | USER, ADMIN           | Unlike a recipe                      |
| POST   | `/api/recipes/comments/{recipeId}`            | USER, ADMIN           | Add comment                          |
| GET    | `/api/recipes/comments/{recipeId}`            | USER, ADMIN           | Get all comments                     |
| GET    | `/admin/audit/all`                            | ADMIN only            | View complete audit logs             |

> All protected endpoints require **Bearer Token** in `Authorization` header

---

### Role-Based Permissions Summary

| Role  | Permissions                                                                 |
|-------|-----------------------------------------------------------------------------|
| ADMIN | Full access + view audit logs + manage everything                          |
| CHEF  | Create/Update/Delete own recipes & categories                              |
| USER  | View recipes, search, like, unlike, comment                                 |

---

### Tech Stack

- Java 21 + Spring Boot 3.x
- Spring Security + JWT Authentication
- JPA/Hibernate + MySQL
- Maven + Lombok
- Multipart file upload with validation

---

### How to Run Locally

```bash
git clone https://github.com/ShreyaCoditas/L-3-Problem-Statement.git
cd L-3-Problem-Statement

# Update application.yml with your DB details
mvn spring-boot:run
