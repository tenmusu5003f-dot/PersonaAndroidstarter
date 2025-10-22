# Generate reports to see what's kept and why
-printusage build/outputs/proguard/usage.txt
-printmapping build/outputs/proguard/mapping.txt
-printconfiguration build/outputs/proguard/configuration.txt
-whyareyoukeeping class com.persona.app.**
-dontnote
-dontwarn
