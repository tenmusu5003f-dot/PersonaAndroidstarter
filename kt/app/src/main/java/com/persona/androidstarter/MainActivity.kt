class MainActivity : AppCompatActivity() {
    private lateinit var session: SessionManager
    private val ui = MainScope()

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_main)
        session = SessionManager(this)
    }

    override fun onStart() {
        super.onStart()
        val nav = (supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment).navController

        ui.launch {
            val ok = withContext(Dispatchers.IO) { FastBoot.launch(this@MainActivity) }
            if (!ok) AuthGate.enforce(nav, session)
            else if (session.isSignedIn()) nav.navigate(R.id.homeFragment)
                 else nav.navigate(R.id.loginFragment)
        }
    }

    override fun onDestroy() { ui.cancel(); super.onDestroy() }
}
