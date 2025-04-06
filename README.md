## Why GraphQL?

Let's say we’re building an app that manages products — nothing fancy. Here's our `Product` entity:

```java
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String category;
    private Float price;
    private Integer stock;
}
```

Now, let’s say different clients start using our backend:

- A **mobile app** wants just the product name and price.
- An **admin panel** needs everything, especially `stock`.
- An **analytics dashboard** only cares about categories and prices.

If we were using REST, we’d probably end up writing a bunch of different DTOs or overloading endpoints with query params like:

```
GET /products?fields=name,price
```

...and then you’d have to manually filter fields, handle edge cases, maybe create custom serializers. It works — but it gets messy real fast.

This is where **GraphQL** shines.

GraphQL flips the idea of “server decides the response shape” on its head — the **client** gets to decide what data it needs. One endpoint, flexible queries, no more DTO explosion.

Here’s an example of how that might look:

```graphql
query {
  getProducts {
    name
    price
  }
}
```

And that’s it. Only `name` and `price` are returned. No boilerplate, no DTOs.

---

## GraphQL & Spring Boot


### 1. Add the dependency

If you're using Maven:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-graphql</artifactId>
</dependency>
```

That’s all you need to get started with GraphQL on the backend.

---

### 2. Define your GraphQL schema

Create a folder at:
```
src/main/resources/graphql/
```

Inside that, add a file called `schema.graphqls`. Here’s what it looks like for our `Product` entity:

```graphql
type Product {
    id: ID
    name: String
    category: String
    price: Float
    stock: Int
}

type Query {
    getProducts: [Product]
    getProductsByCategory(category: String): [Product]
}

type Mutation {
    updateStock(id: ID, stock: Int): Product
    receiveNewShipment(id: ID, stock: Int): Product
}
```

### A quick breakdown:
- `type Product` defines the shape of our data — matches the fields in our Java entity.
- `Query` is for fetching data.
- `Mutation` is for modifying data.

---

### 3. Create your controller

GraphQL in Spring Boot doesn’t need a lot of boilerplate. Here's what a basic controller looks like:

```java
@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @QueryMapping
    public List<Product> getProductsByCategory(@Argument String category) {
        return productService.getProductsByCategory(category);
    }

    @MutationMapping
    public Product updateStock(@Argument int id, @Argument int stock) {
        return productService.updateStock(id, stock);
    }

    @MutationMapping
    public Product receiveNewShipment(@Argument int id, @Argument int stock) {
        return productService.receiveNewShipment(id, stock);
    }
}
```

That’s it. Spring GraphQL automatically hooks this up with your schema file based on the method names.

---


To make life easier while testing, enable the **GraphiQL** playground by adding this to your `application.properties`:

```properties
spring.graphql.graphiql.enabled=true
spring.graphql.graphiql.path=/graphiql
```

Then fire up your browser and go to:
```
http://localhost:8080/graphiql
```

You'll see a nice interface where you can write queries and mutations like this:

```graphql
query {
  getProductsByCategory(category: "Electronics") {
    name
    price
  }
}
```

Or update a product stock:

```graphql
mutation {
  updateStock(id: 1, stock: 30) {
    name
    stock
  }
}
```
