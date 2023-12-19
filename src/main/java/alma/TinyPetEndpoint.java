package alma;

import java.util.Date;
import java.util.HashSet;
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
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.server.spi.auth.EspAuthenticator;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entities;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
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
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;

@Api(name = "petition", version = "v1", clientIds = {
          "911925711183-7rrf2ndcl9e4kc0oaloo5udolinfeu8s.apps.googleusercontent.com" }, audiences = {
                    "911925711183-7rrf2ndcl9e4kc0oaloo5udolinfeu8s.apps.googleusercontent.com" }, namespace = @ApiNamespace(ownerDomain = "tinypet.example.com", ownerName = "tinypet.example.com", packagePath = ""))

public class TinyPetEndpoint {

     @ApiMethod(name = "getPetitions")
     public Entity getPet(@Named("Petname") String name) {
          Query q = new Query("Petition").setFilter(new FilterPredicate("name", null, name));
          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
          PreparedQuery pq = datastore.prepare(q);
          Entity result = pq.asSingleEntity();
          return result;
     }

     @ApiMethod(name = "CreatePetition", httpMethod = ApiMethod.HttpMethod.POST)
     public Entity createPetition(User user, Petition p) throws UnauthorizedException {
          if (user == null) {
               throw new UnauthorizedException("Invalid credentials");
          }

          Entity e = new Entity("Petition", Long.MAX_VALUE-(new Date()).getTime()+":"+user.getEmail());
          e.setProperty("Owner", user.getEmail());
          e.setProperty("name", p.name);
          e.setProperty("SignCount", 0);
          e.setProperty("date", new Date());

          Entity signatories = new Entity("PetitionSignatories", e.getKey());
          HashSet<String> sign=new HashSet<String>();
          signatories.setProperty("signatories", sign);

          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		datastore.put(e);
		datastore.put(signatories);
		txn.commit();
		return e;

     }

     @ApiMethod(name = "sign")
     public void sign(User user, @Named("Petname") String name) throws UnauthorizedException 
     {
          if (user == null) {
               throw new UnauthorizedException("Invalid credentials");
          }

          Query q = new Query("Petition").setFilter(new FilterPredicate("name", null, name));
          DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
          PreparedQuery pq = datastore.prepare(txn, q);
          Entity result = pq.asSingleEntity();
          Query q2 = new Query("PetitionSignatories", result.getKey());
          Entity result2 = datastore.prepare(txn, q2).asSingleEntity();
          HashSet<String> cs = (HashSet<String>) result2.getProperty("signatories");
          if(cs.contains(user.getEmail()))
          {
               txn.rollback();
               throw new UnauthorizedException("Already signed");
          }
          Long newsc = (Long)result.getProperty("signCount") + 1;
          result.setProperty("SignCount", newsc);
          cs.add(user.getEmail());
          result2.setProperty("signatories", cs);
          datastore.put(result);
          datastore.put(result2);
          txn.commit();
          
     }

}