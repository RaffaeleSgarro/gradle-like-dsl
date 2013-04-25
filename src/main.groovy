class Task {
  def String name
  def List<Closure> code = []
}

class Project {

  def String name
  def Map<String, Task> tasks = [:]

  Project(String name) {
    this.name = name
  }

  def task(Task t) {
    tasks.put(t.name, t)
  }

  def methodMissing(String name, args) {
    // WARNING this is not thread safe
    // TODO if it's not a closure, throw an exception
    Task t = tasks.get(name)
    if (t == null) {
      t = new Task()
      t.name = name
      t.code.add ((Closure) args[0])
      return t
    } else {
      t.code.add((Closure) args[0])
    }
  }

  def run(Closure script) {
    script.delegate = this
    script.call()
    tasks.each { String k, Task t ->
      println "\n* Executing task: $k"
      t.code.each { it.call() }
    }
    println "===================================================================="
    println "OK: Project $name has been successfully built"
  }
}

buildScript = {
  task whatAmI {
    println "Configuring project $name"
  }

  whatAmI {
    println "Another statement"
  }

  nonDeclared {
    println "Should not see me"
  }
}

new Project(":foo:bar:baz").run buildScript
