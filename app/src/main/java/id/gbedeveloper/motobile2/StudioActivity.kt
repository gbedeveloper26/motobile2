package id.gbedeveloper.motobile2

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import org.json.JSONObject

/** Tablet-focused studio layout inspired by the user-provided screenshot.
 * Renders one selected motorcycle at a time. BikeCanvas is an illustrative
 * placeholder until properly licensed model-specific 3D geometry is supplied.
 */
class StudioActivity : Activity() {
 private val bg=Color.rgb(12,17,26)
 private val panel=Color.rgb(27,34,45)
 private val blue=Color.rgb(20,94,232)
 private val white=Color.rgb(240,244,250)
 private val muted=Color.rgb(172,184,201)
 private val models=listOf("BeAT Street 2019","BeAT Street 2020","BeAT Street 2026","BeAT 2024","Vario 125","Vario 160","Scoopy","PCX 160","NMAX","Aerox")
 private val categories=listOf("Warna Body","Velg","Ban","Knalpot","Suspensi","Stiker","Jok","Spion","Filter Udara","Aksesoris","Swap Body")
 private val state=Config()
 private lateinit var bike:BikeCanvas
 private lateinit var details:LinearLayout
 private lateinit var editor:LinearLayout
 private lateinit var selectedLabel:TextView
 private lateinit var total:TextView
 private var currentCategory="Warna Body"
 private var before=false
 private fun text(s:String,sp:Float=14f,bold:Boolean=false,color:Int=white)=TextView(this).apply {
  text=s;textSize=sp;setTextColor(color);setPadding(9,8,9,8)
  if(bold)setTypeface(typeface,1)
 }
 private fun column():LinearLayout=LinearLayout(this).apply {orientation=LinearLayout.VERTICAL}
 private fun button(label:String,click:()->Unit):Button=Button(this).apply {
  text=label;textSize=12f;isAllCaps=false;setTextColor(white);setBackgroundColor(panel)
  setOnClickListener {click()}
 }
 private fun spinner(items:List<String>,selected:String,change:(String)->Unit):Spinner {
  val spinner=Spinner(this)
  spinner.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,items)
  spinner.setSelection(items.indexOf(selected).coerceAtLeast(0))
  spinner.onItemSelectedListener=object:android.widget.AdapterView.OnItemSelectedListener {
   override fun onNothingSelected(parent:android.widget.AdapterView<*>?){}
   override fun onItemSelected(parent:android.widget.AdapterView<*>?, view:View?,position:Int,id:Long) {change(items[position]);refresh()}
  }
  return spinner
 }
 private fun scroll(v:View):ScrollView=ScrollView(this).apply{addView(v)}
 private fun addField(label:String,items:List<String>,value:String,change:(String)->Unit) {
  editor.addView(text(label,12f,false,muted))
  editor.addView(spinner(items,value,change))
 }
 override fun onCreate(savedInstanceState:Bundle?) {
  super.onCreate(savedInstanceState)
  window.statusBarColor=bg;window.navigationBarColor=bg
  getSharedPreferences("studio",Context.MODE_PRIVATE).getString("design",null)?.let {
   try {val json=JSONObject(it);state.model=json.optString("model",state.model);state.front=json.optString("front",state.front);state.rear=json.optString("rear",state.rear);state.rim=json.optInt("rim",state.rim);state.body=json.optString("body",state.body);state.mirrors=json.optString("mirrors",state.mirrors);state.airFilter=json.optString("airFilter",state.airFilter);state.exhaust=json.optString("exhaust",state.exhaust);state.accessory=json.optString("accessory",state.accessory);state.color=json.optInt("color",state.color)}catch(_:Exception){}
  }
  if(state.model !in models)state.model=models[1]
  val root=column().apply{setBackgroundColor(bg)}
  val top=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL;setBackgroundColor(panel)}
  top.addView(text("◢ MOTO LAB ID",20f,true),LinearLayout.LayoutParams(0,-2,1f))
  top.addView(button("BeAT"){pickModel("BeAT Street 2020")})
  top.addView(button("Vario 125"){pickModel("Vario 125")})
  top.addView(button("Vario 160"){pickModel("Vario 160")})
  root.addView(top)
  val tablet=resources.configuration.screenWidthDp>=700
  val container=LinearLayout(this).apply{orientation=if(tablet)LinearLayout.HORIZONTAL else LinearLayout.VERTICAL}
  val left=column().apply{setBackgroundColor(panel)}
  left.addView(text("⚒  Modifikasi",18f,true))
  categories.forEach { category ->
   left.addView(button("◈  $category"){currentCategory=category;renderEditor()})
  }
  if(tablet)container.addView(scroll(left),LinearLayout.LayoutParams((resources.configuration.screenWidthDp*.18f*resources.displayMetrics.density).toInt(),-1))
  val middle=column()
  selectedLabel=text(state.model,20f,true)
  middle.addView(selectedLabel)
  middle.addView(text("Studio Modifikasi • geser motor untuk putar 360°",12f,false,muted))
  bike=BikeCanvas(this);bike.config=state
  middle.addView(bike,LinearLayout.LayoutParams(-1,0,1f))
  val controls=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER}
  controls.addView(button("↺ 360°"){bike.angle=(bike.angle+45f)%360f;bike.invalidate()})
  controls.addView(button("Sebelum / Sesudah"){before=!before;bike.config=if(before)Config(model=state.model) else state;bike.invalidate()})
  controls.addView(button("Simpan Desain"){save()})
  middle.addView(controls)
  middle.addView(text("Model dalam build ini masih ilustrasi 2.5D, belum aset 3D resmi Honda.",11f,false,muted))
  if(tablet)container.addView(middle,LinearLayout.LayoutParams(0,-1,1f)) else container.addView(middle,LinearLayout.LayoutParams(-1,(390*resources.displayMetrics.density).toInt()))
  val right=column().apply{setBackgroundColor(panel);setPadding(7,7,7,7)}
  right.addView(text("Detail Modifikasi",19f,true))
  right.addView(text("Pilih motor",12f,false,muted))
  right.addView(spinner(models,state.model){pickModel(it)})
  editor=column();right.addView(editor)
  right.addView(text("Ringkasan & biaya",16f,true))
  details=column();right.addView(details)
  total=text("",20f,true,blue);right.addView(total)
  if(tablet)container.addView(scroll(right),LinearLayout.LayoutParams((resources.configuration.screenWidthDp*.29f*resources.displayMetrics.density).toInt(),-1))
  if(!tablet){container.addView(scroll(left),LinearLayout.LayoutParams(-1,(120*resources.displayMetrics.density).toInt()));container.addView(right)}
  root.addView(if(tablet)container else scroll(container),LinearLayout.LayoutParams(-1,0,1f))
  setContentView(root)
  renderEditor()
 }
 private fun pickModel(value:String) {
  state.model=value;state.rim=if(value.contains("2019")||value.contains("2020"))14 else 12
  state.front=if(state.rim==14)"80/90-14" else "100/90-12"
  state.rear=if(state.rim==14)"90/90-14" else "110/90-12"
  refresh()
 }
 private fun renderEditor() {
  editor.removeAllViews()
  when(currentCategory) {
   "Warna Body"->{
    editor.addView(text("Pilih warna cat",12f,false,muted))
    val row=LinearLayout(this)
    listOf(Color.WHITE,Color.BLACK,Color.RED,blue,Color.GRAY,Color.rgb(214,174,84)).forEach {clr->
     row.addView(button("●"){state.color=clr;refresh()}.apply{setTextColor(clr)},LinearLayout.LayoutParams(0,52,1f))
    }
    editor.addView(row)
   }
   "Velg"->addField("Diameter velg",listOf("12","14"),state.rim.toString()){state.rim=it.toInt()}
   "Ban"->{
    val sizes=listOf("70/90","80/90","90/90","90/100","100/90","110/90","120/80")
    val choices=listOf(12,14).flatMap {dia->sizes.map {"$it-$dia"}}
    addField("Ban depan",choices,state.front){state.front=it}
    addField("Ban belakang",choices,state.rear){state.rear=it}
   }
   "Knalpot"->addField("Model knalpot",listOf("Standar","Racing"),state.exhaust){state.exhaust=it}
   "Spion"->addField("Model spion",listOf("Standar","Sport","Bar-end","Bulat","Tanpa spion"),state.mirrors){state.mirrors=it}
   "Filter Udara"->addField("Filter udara",listOf("Standar","Muttaru Custom"),state.airFilter){state.airFilter=it}
   "Swap Body"->addField("Ganti body",listOf("Original","BeAT Street 2019","BeAT Street 2020","BeAT Street 2026"),state.body){state.body=it}
   "Aksesoris"->addField("Aksesori",listOf("Tanpa aksesori","Handguard","Visor","Holder HP","Bracket box","Footstep","Grip custom"),state.accessory){state.accessory=it}
   else->editor.addView(text("Katalog part $currentCategory: dibutuhkan aset 3D dan data produk untuk penggantian realistis.",13f,false,muted))
  }
  refresh()
 }
 private fun refresh() {
  if(!::bike.isInitialized)return
  selectedLabel.text=state.model
  if(!before){bike.config=state;bike.invalidate()}
  if(!::details.isInitialized)return
  details.removeAllViews()
  val invalid=!state.front.endsWith("-${state.rim}")||!state.rear.endsWith("-${state.rim}")
  val caution=state.body!="Original"||state.airFilter!="Standar"||state.accessory!="Tanpa aksesori"
  val entries=listOf("Body" to state.body,"Velg" to "${state.rim} inci","Ban depan" to state.front,"Ban belakang" to state.rear,"Knalpot" to state.exhaust,"Spion" to state.mirrors,"Filter udara" to state.airFilter,"Aksesori" to state.accessory)
  entries.forEach { (k,v)->details.addView(text("$k  •  $v",12f)) }
  details.addView(text(when {
   invalid->"⛔ Tidak kompatibel: diameter ban tidak sesuai velg."
   caution->"⚠ Perlu cek bengkel: ukuran dudukan dan kompatibilitas fisik belum diverifikasi."
   else->"✓ Diameter ban dan velg sesuai. Tetap cek lebar dan clearance."
  },12f,true,if(invalid)Color.rgb(255,118,135) else if(caution)Color.rgb(255,204,112) else Color.rgb(121,223,181)))
  val estimate=(if(state.exhaust=="Racing")1350000 else 0)+(if(state.airFilter!="Standar")350000 else 0)+(if(state.body!="Original")1250000 else 0)
  total.text="Estimasi mulai Rp ${String.format("%,d",estimate).replace(',', '.')}"
 }
 private fun save() {
  val json=JSONObject()
  json.put("model",state.model);json.put("rim",state.rim);json.put("front",state.front);json.put("rear",state.rear)
  json.put("body",state.body);json.put("mirrors",state.mirrors);json.put("airFilter",state.airFilter)
  json.put("exhaust",state.exhaust);json.put("accessory",state.accessory);json.put("color",state.color)
  getSharedPreferences("studio",Context.MODE_PRIVATE).edit().putString("design",json.toString()).apply()
  Toast.makeText(this,"Desain disimpan di perangkat",Toast.LENGTH_SHORT).show()
 }
}
