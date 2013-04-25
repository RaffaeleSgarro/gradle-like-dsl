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

  def methodMissing(String name, Object args) {
    if (args.length != 1 || !(args[0] instanceof Closure)) {
      throw new MissingMethodException(name, Project.class, args)
    }

    Closure c = (Closure) args[0]
    Task t = tasks.get(name)
    if (t == null) {
      t = new Task()
      t.name = name
      t.code.add(c)
      return t
    } else {
      t.code.add(c)
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

// Instead of reading an external file, just use this for the sake
// of simplicity
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

new Project("My aswesome app").run buildScript
