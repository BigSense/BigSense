package io.bigsense;

public aspect AspectLogger {

  private long start;

  pointcut conn() : call(* io.bigsense.server.TomcatServer.startServer(..));

  void around() : conn() {
    start = System.currentTimeMillis();
    Throwable t = null;
    try {
      proceed();
    }
    catch(Throwable _t) {
      t = _t;
    }
    long spent = System.currentTimeMillis() - start;
    System.out.println("Send time: " + spent);
    if (t != null) {
      throw new RuntimeException(t);
    }
  }

}