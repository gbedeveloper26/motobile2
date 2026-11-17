package id.gbedeveloper.motobile2

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.*
import android.view.View
import org.json.JSONObject

/** Image based prototype. Replace drawable motorcycle_cutout with licensed, model-specific photo layers. */
class StudioActivity:Activity() {
 private val bikes=listOf("BeAT Street 2019","BeAT Street 2020","BeAT Street 2026","BeAT 2024","Vario 125","Vario 160","Scoopy","PCX 160","NMAX","Aerox")
 private val categories=listOf("Warna Body","Velg","Ban","Knalpot","Suspensi","Stiker","Jok","Spion","Filter Udara","Aksesoris","Swap Body")
 private val dark=Color.rgb(18,23,33);private val panel=Color.rgb(29,35,48);private val blue=Color.rgb(25,100,237)
 private var bikeName=bikes[1];private var category="Warna Body";private var size=14
 private var front="80/90-14";private var rear="90/90-14";private var body="Original";private var exhaust="Standar";private var mirror="Standar";private var filter="Standar";private var accessory="Tanpa aksesori";private var suspension="Standar";private var seat="Standar";private var sticker="Tanpa stiker";private var tint=Color.WHITE
 private lateinit var image:ImageView;private lateinit var heading:TextView;private lateinit var settings:LinearLayout;private lateinit var summary:LinearLayout
 private fun txt(t:String,sp:Float=14f,bold:Boolean=false)=TextView(this).apply { text=t;textSize=sp;setTextColor(Color.WHITE);setPadding(9,9,9,9);if(bold)typeface=Typeface.DEFAULT_BOLD }
 private fun col()=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL}
 private fun row()=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL}
 private fun button(t:String,f:()->Unit)=Button(this).apply{text=t;textSize=12f;isAllCaps=false;setTextColor(Color.WHITE);background=GradientDrawable().apply{setColor(blue);cornerRadius=14f};setOnClickListener{f()}}
 private fun choice(label:String,options:List<String>,value:String,onSelect:(String)->Unit){
  settings.addView(txt(label,12f,true))
  val spinner=Spinner(this)
  spinner.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,options)
  spinner.setSelection(options.indexOf(value).coerceAtLeast(0))
  spinner.onItemSelectedListener=object:android.widget.AdapterView.OnItemSelectedListener{
   override fun onNothingSelected(parent:android.widget.AdapterView<*>?){}
   override fun onItemSelected(p:android.widget.AdapterView<*>?,v:View?,index:Int,id:Long){onSelect(options[index]);refresh()}
  }
  settings.addView(spinner)
 }
 override fun onCreate(saved:Bundle?){super.onCreate(saved)
  window.statusBarColor=dark;window.navigationBarColor=dark
  val prefs=getSharedPreferences("editor",MODE_PRIVATE)
  prefs.getString("design",null)?.let{try{val j=JSONObject(it);bikeName=j.optString("bike",bikeName);size=j.optInt("size",size);front=j.optString("front",front);rear=j.optString("rear",rear);body=j.optString("body",body);exhaust=j.optString("exhaust",exhaust);mirror=j.optString("mirror",mirror);filter=j.optString("filter",filter);accessory=j.optString("accessory",accessory);tint=j.optInt("tint",tint)}catch(_:Exception){}}
  val root=col().apply{setBackgroundColor(dark)}
  val header=row().apply{setBackgroundColor(panel)}
  header.addView(txt("◢ MOTO LAB ID",20f,true),LinearLayout.LayoutParams(0,-2,1f))
  header.addView(button("BeAT"){pick(bikes[1])});header.addView(button("Vario"){pick(bikes[4])})
  root.addView(header)
  val wide=resources.configuration.screenWidthDp>=700
  val workspace=if(wide)row()else col()
  val left=col().apply{setBackgroundColor(panel);setPadding(6,8,6,8)}
  left.addView(txt("⚒ MODIFIKASI",16f,true))
  categories.forEach{c->left.addView(button(c){category=c;renderSettings()})}
  val middle=col().apply{setBackgroundColor(Color.rgb(70,77,88))}
  heading=txt(bikeName,20f,true);middle.addView(heading)
  middle.addView(txt("Satu motor • editor berbasis gambar",12f))
  image=ImageView(this).apply{setImageResource(R.drawable.motorcycle_cutout);scaleType=ImageView.ScaleType.FIT_CENTER;setPadding(12,12,12,12)}
  middle.addView(image,LinearLayout.LayoutParams(-1,0,1f))
  middle.addView(txt("Ilustrasi sementara. Foto motor asli dan gambar part per layer belum disediakan.",11f))
  val bar=row()
  bar.addView(button("Simpan desain"){val j=JSONObject();j.put("bike",bikeName);j.put("size",size);j.put("front",front);j.put("rear",rear);j.put("body",body);j.put("exhaust",exhaust);j.put("mirror",mirror);j.put("filter",filter);j.put("accessory",accessory);j.put("tint",tint);prefs.edit().putString("design",j.toString()).apply();Toast.makeText(this,"Desain tersimpan",Toast.LENGTH_SHORT).show()})
  bar.addView(button("Reset"){tint=Color.WHITE;body="Original";exhaust="Standar";filter="Standar";mirror="Standar";refresh()})
  middle.addView(bar)
  val right=col().apply{setPadding(8,6,8,6);setBackgroundColor(panel)}
  right.addView(txt("DETAIL MODIFIKASI",17f,true))
  settings=col();right.addView(settings)
  summary=col();right.addView(summary)
  val pick=Spinner(this);pick.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,bikes);pick.setSelection(bikes.indexOf(bikeName).coerceAtLeast(0))
  pick.onItemSelectedListener=object:android.widget.AdapterView.OnItemSelectedListener{
    override fun onNothingSelected(parent:android.widget.AdapterView<*>?){}
    override fun onItemSelected(p:android.widget.AdapterView<*>?,v:View?,i:Int,id:Long){pick(bikes[i])}
  };right.addView(pick,0)
  if(wide){
   val density=resources.displayMetrics.density
   workspace.addView(ScrollView(this).apply{addView(left)},LinearLayout.LayoutParams((190*density).toInt(),-1))
   workspace.addView(middle,LinearLayout.LayoutParams(0,-1,1f))
   workspace.addView(ScrollView(this).apply{addView(right)},LinearLayout.LayoutParams((280*density).toInt(),-1))
   root.addView(workspace,LinearLayout.LayoutParams(-1,0,1f))
  }else{
   workspace.addView(middle,LinearLayout.LayoutParams(-1,(350*resources.displayMetrics.density).toInt()))
   workspace.addView(right);workspace.addView(left)
   root.addView(ScrollView(this).apply{addView(workspace)},LinearLayout.LayoutParams(-1,0,1f))
  }
  setContentView(root);renderSettings()
 }
 private fun pick(name:String){
  if(bikeName!=name){bikeName=name;size=if(name.contains("2019")||name.contains("2020"))14 else 12;front=if(size==14)"80/90-14" else "100/90-12";rear=if(size==14)"90/90-14" else "110/90-12"}
  refresh()
 }
 private fun renderSettings(){
  settings.removeAllViews()
  when(category){
   "Warna Body"->{settings.addView(txt("Warna body",13f,true));val r=row()
    listOf(Color.WHITE,Color.BLACK,Color.RED,blue,Color.GRAY,Color.rgb(201,154,58)).forEach{color->r.addView(button("●"){tint=color;refresh()}.apply{setTextColor(color)},LinearLayout.LayoutParams(0,54,1f))}
    settings.addView(r)
   }
   "Velg"->choice("Diameter velg",listOf("12","14"),size.toString()){size=it.toInt()}
   "Ban"->{val sizes=listOf("70/90","80/90","90/90","90/100","100/90","110/90","120/80");val options=listOf(12,14).flatMap{r->sizes.map{"$it-$r"}}
    choice("Ban depan",options,front){front=it};choice("Ban belakang",options,rear){rear=it}}
   "Knalpot"->choice("Knalpot",listOf("Standar","Racing"),exhaust){exhaust=it}
   "Spion"->choice("Spion",listOf("Standar","Sport","Bar-end","Bulat","Tanpa spion"),mirror){mirror=it}
   "Filter Udara"->choice("Filter",listOf("Standar","Muttaru Custom"),filter){filter=it}
   "Swap Body"->choice("Body",listOf("Original","BeAT Street 2019","BeAT Street 2020","BeAT Street 2026"),body){body=it}
   "Aksesoris"->choice("Aksesori",listOf("Tanpa aksesori","Handguard","Visor","Holder HP","Bracket box","Footstep"),accessory){accessory=it}
   "Suspensi"->choice("Suspensi",listOf("Standar","Sport","Adjustable"),suspension){suspension=it}
   "Stiker"->choice("Stiker",listOf("Tanpa stiker","Sport","Racing"),sticker){sticker=it}
   "Jok"->choice("Jok",listOf("Standar","Custom kulit","Carbon"),seat){seat=it}
  };refresh()
 }
 private fun refresh(){
  if(!::image.isInitialized)return
  heading.text=bikeName
  image.setColorFilter(tint,PorterDuff.Mode.MULTIPLY)
  summary.removeAllViews()
  listOf("Velg" to "$size inci","Ban depan" to front,"Ban belakang" to rear,"Body" to body,"Spion" to mirror,"Filter" to filter,"Knalpot" to exhaust,"Aksesori" to accessory,"Suspensi" to suspension,"Stiker" to sticker,"Jok" to seat).forEach{summary.addView(txt("${it.first}: ${it.second}",12f))}
  val bad=!front.endsWith("-$size")||!rear.endsWith("-$size")
  val caution=body!="Original"||filter!="Standar"||accessory!="Tanpa aksesori"
  summary.addView(txt(if(bad)"TIDAK KOMPATIBEL: diameter ban dan velg berbeda." else if(caution)"PERLU CEK BENGKEL: dudukan/ukuran body dan aksesori belum diverifikasi." else "Diameter ban dan velg sesuai; lebar dan clearance perlu dicek.",12f,true))
  summary.addView(txt("Harga: belum tersedia • penggantian part visual perlu aset foto transparan.",12f))
 }
}