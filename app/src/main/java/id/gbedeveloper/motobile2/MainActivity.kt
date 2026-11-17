package id.gbedeveloper.motobile2

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.graphics.*
import android.view.*
import android.widget.*
import org.json.JSONObject
import kotlin.math.*

data class Config(
 var model:String="Honda BeAT Street 2020", var rim:Int=14,
 var front:String="80/90-14", var rear:String="90/90-14",
 var body:String="Original", var airFilter:String="Standar",
 var mirrors:String="Standar", var exhaust:String="Standar",
 var accessory:String="Tanpa aksesori", var color:Int=Color.rgb(41,118,210)
)
class BikeCanvas(context:Context):View(context) {
 var angle=0f; var zoom=1f; var config=Config()
 private val p=Paint(3)
 private var lastX=0f; private var pinchDistance=0f
 private fun circle(c:Canvas,x:Float,y:Float,r:Float,color:Int,stroke:Float=0f){
  p.color=color;p.style=if(stroke>0)Paint.Style.STROKE else Paint.Style.FILL;p.strokeWidth=stroke;c.drawCircle(x,y,r,p);p.style=Paint.Style.FILL
 }
 private fun line(c:Canvas,x:Float,y:Float,x2:Float,y2:Float,color:Int,w:Float){
  p.color=color;p.strokeWidth=w;p.strokeCap=Paint.Cap.ROUND;c.drawLine(x,y,x2,y2,p)
 }
 override fun onDraw(c:Canvas){
  super.onDraw(c); c.drawColor(Color.rgb(12,23,39))
  val w=width.toFloat();val h=height.toFloat(); c.save()
  c.translate(w/2,h*.54f);c.scale(zoom,zoom)
  val scale=min(w/440f,h/360f);c.scale(scale,scale)
  val phase=Math.toRadians(angle.toDouble());val side=cos(phase).toFloat()
  val depth=sin(phase).toFloat()
  p.color=Color.rgb(25,44,58);c.drawOval(-195f,91f,195f,112f,p)
  val sx=if(side>=0)1f else -1f
  c.scale(sx,1f)
  val wheelSpacing=155f*abs(side)+55f*(1-abs(side))
  for(x in listOf(-wheelSpacing,wheelSpacing)){
   c.save();c.translate(x,64f)
   p.color=Color.BLACK;p.style=Paint.Style.STROKE;p.strokeWidth=17f
   c.drawOval(-45f+depth*5,-47f,45f+depth*5,47f,p)
   p.color=Color.rgb(197,206,225);p.strokeWidth=8f;c.drawOval(-30f,-32f,30f,32f,p)
   for(i in 0..7){val t=i*Math.PI/4;line(c,0f,0f,(cos(t)*29).toFloat(),(sin(t)*29).toFloat(),Color.rgb(130,146,174),2.5f)}
   p.style=Paint.Style.FILL;c.restore()
  }
  line(c,-wheelSpacing,63f,-45f,-21f,Color.GRAY,9f)
  line(c,wheelSpacing,63f,65f,-81f,Color.LTGRAY,11f)
  p.color=config.color
  val body=Path().apply {moveTo(-155f,24f);lineTo(-112f,-33f);lineTo(40f,-36f);lineTo(86f,12f);lineTo(36f,47f);lineTo(-110f,47f);close()}
  c.drawPath(body,p)
  p.color=Color.rgb(19,26,36);c.drawRoundRect(-102f,-55f,5f,-35f,12f,12f,p)
  p.color=config.color;c.drawRoundRect(57f,-133f,115f,14f,15f,15f,p)
  p.color=Color.CYAN;c.drawRoundRect(78f,-109f,117f,-89f,9f,9f,p)
  p.color=Color.rgb(48,52,62);c.drawRoundRect(-130f,32f,12f,48f,5f,5f,p)
  line(c,85f,-147f,85f,-181f,Color.LTGRAY,6f)
  line(c,45f,-180f,133f,-180f,Color.DKGRAY,9f)
  if(config.mirrors!="Tanpa spion"){
   line(c,45f,-180f,26f,-212f,Color.LTGRAY,4f)
   line(c,129f,-180f,148f,-212f,Color.LTGRAY,4f)
   p.color=Color.GRAY;c.drawOval(4f,-222f,46f,-207f,p);c.drawOval(127f,-222f,170f,-207f,p)
  }
  p.color=if(config.exhaust=="Racing")Color.rgb(214,142,72) else Color.rgb(76,85,103)
  c.drawRoundRect(-166f,44f,-65f,64f,7f,7f,p)
  p.color=Color.WHITE;p.textSize=15f;p.textAlign=Paint.Align.CENTER
  c.drawText("Geser untuk putar 360° • Cubit untuk zoom",0f,160f,p)
  p.textSize=12f;p.color=Color.rgb(160,184,213);c.drawText("Ilustrasi 2.5D • bukan geometri motor asli",0f,180f,p)
  c.restore()
 }
 override fun onTouchEvent(e:android.view.MotionEvent):Boolean{
  when(e.actionMasked){
   MotionEvent.ACTION_DOWN->{lastX=e.x;return true}
   MotionEvent.ACTION_POINTER_DOWN->{if(e.pointerCount>=2)pinchDistance=hypot(e.getX(1)-e.getX(0),e.getY(1)-e.getY(0));return true}
   MotionEvent.ACTION_MOVE->{
    if(e.pointerCount>=2){
     val d=hypot(e.getX(1)-e.getX(0),e.getY(1)-e.getY(0))
     if(pinchDistance>0)zoom=(zoom*d/pinchDistance).coerceIn(.65f,2.2f)
     pinchDistance=d
    }else{angle=(angle+(e.x-lastX)*.65f)%360f;lastX=e.x}
    invalidate();return true
   }
   MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{pinchDistance=0f;return true}
  }
  return true
 }
}
class MainActivity:Activity(){
 private val config=Config()
 private lateinit var canvas:BikeCanvas
 private lateinit var status:TextView
 private val models=listOf("Honda BeAT Street 2019","Honda BeAT Street 2020","Honda BeAT Street 2026","Honda BeAT 2024","Honda BeAT 2026","Honda Vario 125","Honda Vario 160","Honda Scoopy","Honda PCX 160","Yamaha NMAX","Yamaha Aerox","Yamaha Fazzio")
 private val sizes=listOf("70/90","80/90","90/90","90/100","100/90","110/90","120/80")
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState)
  window.statusBarColor=Color.rgb(9,18,31);window.navigationBarColor=Color.rgb(9,18,31)
  val prefs=getSharedPreferences("design",MODE_PRIVATE)
  try{val j=JSONObject(prefs.getString("config","{}")?:"{}");config.model=j.optString("model",config.model);config.rim=j.optInt("rim",14);config.front=j.optString("front",config.front);config.rear=j.optString("rear",config.rear);config.body=j.optString("body","Original");config.airFilter=j.optString("airFilter","Standar");config.mirrors=j.optString("mirrors","Standar");config.exhaust=j.optString("exhaust","Standar");config.accessory=j.optString("accessory","Tanpa aksesori");config.color=j.optInt("color",config.color)}catch(_:Exception){}
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setBackgroundColor(Color.rgb(9,18,31))}
  val scroll=ScrollView(this);val content=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,12,18,28)}
  fun label(t:String,size:Float=15f):TextView=TextView(this).apply{text=t;textSize=size;setTextColor(Color.WHITE);setPadding(0,9,0,5)}
  fun selector(title:String, options:List<String>, current:()->String, update:(String)->Unit){
   content.addView(label(title))
   val s=Spinner(this);val a=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,options);s.adapter=a
   s.setSelection(options.indexOf(current()).coerceAtLeast(0))
   s.onItemSelectedListener=object:android.widget.AdapterView.OnItemSelectedListener{
    override fun onNothingSelected(parent:android.widget.AdapterView<*>?){}
    override fun onItemSelected(parent:android.widget.AdapterView<*>?,v:View?,pos:Int,id:Long){update(options[pos]);refresh()}
   };content.addView(s)
  }
  root.addView(label("MOTO LAB ID  •  Android Native",23f).apply{setPadding(18,12,0,6)})
  canvas=BikeCanvas(this);canvas.config=config
  root.addView(canvas,LinearLayout.LayoutParams(-1,0,1f))
  root.addView(label("Satu motor • Putar 360° dengan geser • Pinch zoom",12f).apply{setPadding(18,5,0,6)})
  status=label("",13f);root.addView(status.apply{setPadding(18,8,18,8)})
  selector("Pilih motor",models,{config.model}){
   config.model=it;config.rim=if(it.contains("2019")||it.contains("2020"))14 else 12
   config.front=if(config.rim==14)"80/90-14" else "100/90-12";config.rear=if(config.rim==14)"90/90-14" else "110/90-12"
  }
  selector("Velg",listOf("12","14"),{config.rim.toString()}){config.rim=it.toInt()}
  val allTires=(listOf(12,14)).flatMap{r->sizes.map{"$it-$r"}}
  selector("Ban depan",allTires,{config.front}){config.front=it}
  selector("Ban belakang",allTires,{config.rear}){config.rear=it}
  selector("Body swap",listOf("Original","BeAT Street 2019","BeAT Street 2020","BeAT Street 2026"),{config.body}){config.body=it}
  selector("Filter udara",listOf("Standar","Muttaru Custom"),{config.airFilter}){config.airFilter=it}
  selector("Spion",listOf("Standar","Sport","Bar-end","Bulat","Tanpa spion"),{config.mirrors}){config.mirrors=it}
  selector("Knalpot",listOf("Standar","Racing"),{config.exhaust}){config.exhaust=it}
  selector("Aksesori",listOf("Tanpa aksesori","Handguard","Visor","Holder HP","Bracket box","Footstep","Grip custom"),{config.accessory}){config.accessory=it}
  content.addView(label("Warna body"))
  val colors=listOf(Color.rgb(41,118,210),Color.BLACK,Color.WHITE,Color.RED,Color.rgb(226,175,56),Color.GRAY)
  val row=LinearLayout(this);colors.forEach{clr->val b=Button(this);b.text="●";b.setTextColor(clr);b.setOnClickListener{config.color=clr;refresh()};row.addView(b,LinearLayout.LayoutParams(0,56,1f))};content.addView(row)
  val save=Button(this);save.text="Simpan desain lokal";save.setOnClickListener{val j=JSONObject();j.put("model",config.model);j.put("rim",config.rim);j.put("front",config.front);j.put("rear",config.rear);j.put("body",config.body);j.put("airFilter",config.airFilter);j.put("mirrors",config.mirrors);j.put("exhaust",config.exhaust);j.put("accessory",config.accessory);j.put("color",config.color);prefs.edit().putString("config",j.toString()).apply();Toast.makeText(this,"Desain tersimpan",Toast.LENGTH_SHORT).show()};content.addView(save)
  scroll.addView(content);root.addView(scroll,LinearLayout.LayoutParams(-1,0,1.2f))
  setContentView(root);refresh()
 }
 private fun refresh(){
  if(!::status.isInitialized)return
  canvas.invalidate()
  val mismatch=!config.front.endsWith("-${config.rim}")||!config.rear.endsWith("-${config.rim}")
  val caution=config.body!="Original"||config.airFilter!="Standar"||config.accessory!="Tanpa aksesori"
  status.text=when{
   mismatch->"TIDAK KOMPATIBEL: diameter ban dan velg berbeda. Pilih ukuran dengan akhiran -${config.rim}."
   caution->"PERLU VERIFIKASI BENGKEL: dudukan body/filter/aksesori, dimensi, baut, dan ruang bebas belum terukur."
   else->"Ukuran diameter ban/velg sesuai; lebar ban dan clearance tetap harus diperiksa."
  }
  status.setTextColor(if(mismatch)Color.rgb(255,126,145) else if(caution)Color.rgb(255,209,132) else Color.rgb(149,234,194))
 }
}
