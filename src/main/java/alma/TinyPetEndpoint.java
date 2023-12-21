package alma;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.server.spi.auth.EspAuthenticator;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entities;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.datastore.v1.PropertyFilter;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;

import alma.Petition;

@Api(name = "petition", version = "v1", clientIds = {
          "911925711183-7rrf2ndcl9e4kc0oaloo5udolinfeu8s.apps.googleusercontent.com" }, audiences = {
                    "911925711183-7rrf2ndcl9e4kc0oaloo5udolinfeu8s.apps.googleusercontent.com" }, namespace = @ApiNamespace(ownerDomain = "tinypet.example.com", ownerName = "tinypet.example.com", packagePath = ""))

public class TinyPetEndpoint {

     @ApiMethod(name = "getPetitions")
     public Entity getPet(@Named("Petname") String name) {
          Query q = new Query("Petition").setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));
          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
          PreparedQuery pq = datastore.prepare(q);
          Entity result = pq.asSingleEntity();
          return result;
     }

     @ApiMethod(name = "CreatePetition", httpMethod = ApiMethod.HttpMethod.POST)
     public Entity createPetition(User user, alma.Petition p) throws UnauthorizedException {
          if (user == null) {
               throw new UnauthorizedException("Invalid credentials");
          }

          long id = Long.MAX_VALUE - ((new Date()).getTime() + user.getEmail().hashCode());

          Key petitionKey = KeyFactory.createKey("Petition", id);
          Entity e = new Entity(petitionKey);

          e.setProperty("Owner", user.getEmail());
          e.setProperty("name", p.name.replace(' ', '+'));
          e.setProperty("SignCount", 0);
          e.setProperty("date", new Date());

          Key signatoriesKey = KeyFactory.createKey(petitionKey, "PetitionSignatories", id);
          Entity signatories = new Entity(signatoriesKey);
          List<String> sign = new ArrayList<String>();
          signatories.setProperty("signatories", sign);

          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
          Transaction txn = datastore.beginTransaction();
          datastore.put(e);
          datastore.put(signatories);
          txn.commit();
          return e;

     }

     @ApiMethod(name = "sign")
     public Entity sign(User user, @Named("Petname") String name) throws UnauthorizedException {
          if (user == null) {
               throw new UnauthorizedException("Invalid credentials");
          }

          String email = user.getEmail();
          name = name.replace(" ", "+");

          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

          Query q = new Query("Petition").setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));
          PreparedQuery pq = datastore.prepare(q);
          Entity result = pq.asSingleEntity();
          Key petitionKey = result.getKey();

          Transaction txn = datastore.beginTransaction();
          Query q2 = new Query("PetitionSignatories").setAncestor(petitionKey);
          Entity result2 = datastore.prepare(txn, q2).asSingleEntity();
          List<String> cs = (List<String>) result2.getProperty("signatories");

          if (cs == null)
               cs = new ArrayList<>();

          if (cs.contains(email)) {
               txn.rollback();
               throw new UnauthorizedException("Already signed");
          }

          Long newsc = (Long) result.getProperty("SignCount") + 1;
          result.setProperty("SignCount", newsc);
          cs.add(email);
          result2.setProperty("signatories", cs);

          datastore.put(result);
          datastore.put(result2);

          txn.commit();

          return result;

     }

     @ApiMethod(name = "unsafeCreatePet", httpMethod = ApiMethod.HttpMethod.POST)
     public Entity createPetUnsafe(@Named("UserEmail") String email, alma.Petition p) {

          long id = Long.MAX_VALUE - ((new Date()).getTime() + email.hashCode());

          Key petitionKey = KeyFactory.createKey("Petition", id);
          Entity e = new Entity(petitionKey);

          e.setProperty("Owner", email);
          e.setProperty("name", p.name.replace(' ', '+'));
          e.setProperty("SignCount", 0);
          e.setProperty("date", new Date());

          Key signatoriesKey = KeyFactory.createKey(petitionKey, "PetitionSignatories", id);
          Entity signatories = new Entity(signatoriesKey);
          List<String> sign = new ArrayList<String>();
          signatories.setProperty("signatories", sign);

          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
          Transaction txn = datastore.beginTransaction();
          datastore.put(e);
          datastore.put(signatories);
          txn.commit();
          return e;
     }

     @ApiMethod(name = "unsafeSign", httpMethod = HttpMethod.GET)
     public Entity signUnsafe(@Named("UserEmail") String email, @Named("Petname") String name)
               throws UnauthorizedException, NotFoundException {

          name = name.replace(" ", "+");

          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

          Query q = new Query("Petition").setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));
          PreparedQuery pq = datastore.prepare(q);
          Entity result = pq.asSingleEntity();

          if (result == null)
               throw new NotFoundException("pet with name " + name + "not found");

          Key petitionKey = result.getKey();

          Transaction txn = datastore.beginTransaction();
          Query q2 = new Query("PetitionSignatories").setAncestor(petitionKey);
          Entity result2 = datastore.prepare(txn, q2).asSingleEntity();
          List<String> cs = (ArrayList<String>) result2.getProperty("signatories");

          if (cs == null)
               cs = new ArrayList<>();

          if (cs.contains(email)) {
               txn.rollback();
               throw new UnauthorizedException("Already signed");
          }

          Long newsc = (Long) result.getProperty("SignCount") + 1;
          result.setProperty("SignCount", newsc);
          cs.add(email);
          result2.setProperty("signatories", cs);

          datastore.put(result);
          datastore.put(result2);

          txn.commit();

          return result;
     }

     @ApiMethod(name = "getSignedBy", httpMethod = HttpMethod.GET)
     public List<Entity> getSignedBy(@Named("userEmail") String email) {
          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

          List<Entity> pets = new ArrayList<>();
          Query q = new Query("PetitionSignatories")
                    .setFilter(new FilterPredicate("signatories", FilterOperator.EQUAL, email));
          PreparedQuery pq = datastore.prepare(q);
          List<Entity> r1 = pq.asList(FetchOptions.Builder.withLimit(250));
          List<Key> k = new ArrayList<>();
          for (Entity e : r1) {
               k.add(e.getParent());
          }

          Query q2 = new Query("Petition").setFilter(new FilterPredicate("__key__", FilterOperator.IN, k));
          PreparedQuery pq2 = datastore.prepare(q2);
          pets = pq2.asList(FetchOptions.Builder.withLimit(250));

          return pets;
     }

     @ApiMethod(name = "topPets", httpMethod = HttpMethod.GET)
     public List<Entity> topPets() {
          List<Entity> e;

          Query q = new Query("Petition").addSort("SignCount", SortDirection.DESCENDING);
          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
          PreparedQuery pq = datastore.prepare(q);
          e = pq.asList(FetchOptions.Builder.withLimit(100));

          return e;
     }

     @ApiMethod(name = "RandomPet", httpMethod = HttpMethod.GET)
     public Entity createRpet() {
          alma.Petition p = new Petition();

          p.body = "<p> pet body <p>";
          p.name = "random pet at " + new Date();

          String email = "fake@email.com";

          long id = Long.MAX_VALUE - ((new Date()).getTime() + email.hashCode());

          Key petitionKey = KeyFactory.createKey("Petition", id);
          Entity e = new Entity(petitionKey);

          e.setProperty("Owner", email);
          e.setProperty("name", p.name.replace(' ', '+'));
          e.setProperty("SignCount", 0);
          e.setProperty("date", new Date());

          Key signatoriesKey = KeyFactory.createKey(petitionKey, "PetitionSignatories", id);
          Entity signatories = new Entity(signatoriesKey);
          List<String> sign = new ArrayList<String>();
          signatories.setProperty("signatories", sign);

          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
          Transaction txn = datastore.beginTransaction();
          datastore.put(e);
          datastore.put(signatories);
          txn.commit();
          return e;

     }
}