& 'C:\Program Files\Java\jdk1.8.0_131\bin\javapackager.exe' `
  -deploy `
  -native `
  -outdir packages `
  -outfile StopWatch `
  -srcdir target/scala-2.10/ `
  -srcfiles fx-stopwatch.jar `
  -appclass com.yumusoft.stopwatch.Main  `
  -name "StopWatch" `
  -title "JavaFX-based StopWatch"
