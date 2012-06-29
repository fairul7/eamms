/**
 * GetJokeSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.joke;

public interface GetJokeSoap extends java.rmi.Remote {

    // Joke categories:<br><br>Murphy's Laws - 7<br>Q&A - 3<br>Unnatural
    // Laws - 18<br>Cool Jokes - 6<br>Blondes - 2<br>Random(contains Adult)
    // - 1<br>Lawyers - 5<br>Headlines - 8<br>Military - 9<br>Accidents -
    // 4<br>Excuses - 10<br>Answering machine - 11<br>All categories - 0<br><br>
    public java.lang.String getJoke(java.lang.String category) throws java.rmi.RemoteException;
}
