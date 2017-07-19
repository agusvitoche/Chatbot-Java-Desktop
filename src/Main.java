
import org.alicebot.ab.*;
import java.io.*;
import java.util.HashMap;
/*import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;*/

public class Main {
	
	public static void main(String[]args){
		MagicStrings.setRootPath("C:\\eclipse-workspace\\Chatbot\\src");
	    AIMLProcessor.extension =  new PCAIMLProcessorExtension();
		mainFunction(args);
	}
	public static void mainFunction (String[] args){
		
        String botName = "prueba";
        MagicBooleans.jp_tokenize = false;
        MagicBooleans.trace_mode = true;
        //String action="aiml2csv";
        String action="chat";
        System.out.println(MagicStrings.program_name_version);
        
        for (String s : args) {
            String[] splitArg = s.split("=");
            if (splitArg.length >= 2) {
                String option = splitArg[0];
                String value = splitArg[1];
                if (option.equals("bot")) botName = value;
                if (option.equals("action")) action = value;
                if (option.equals("trace")) {
                    if (value.equals("true")) MagicBooleans.trace_mode = true;
                    else MagicBooleans.trace_mode = false;
                }
                if (option.equals("morph")) {
                    if (value.equals("true")) MagicBooleans.jp_tokenize = true;
                    else {
                        MagicBooleans.jp_tokenize = false;
                    }
                }
             }
        }
        
        if (MagicBooleans.trace_mode) System.out.println("Working Directory = " + MagicStrings.root_path);
        Graphmaster.enableShortCuts = true;
        Bot bot = new Bot(botName, MagicStrings.root_path, action); //
        if (MagicBooleans.make_verbs_sets_maps) Verbs.makeVerbSetsMaps(bot);
        if (bot.brain.getCategories().size() < MagicNumbers.brain_print_size) bot.brain.printgraph();
        if (MagicBooleans.trace_mode) System.out.println("Action = '"+action+"'");
        if (action.equals("chat") || action.equals("chat-app")) {
        	Chatbot chatbot=new Chatbot(bot);
        	chatbot.setBounds(0,0,600,400);
        	chatbot.setVisible(true);
		}
        else if (action.equals("ab")) TestAB.testAB(bot, TestAB.sample_file);
        else if (action.equals("aiml2csv") || action.equals("csv2aiml")) convert(bot, action);
        else if (action.equals("abwq")){AB ab = new AB(bot, TestAB.sample_file);  ab.abwq();}
		else if (action.equals("test")) { TestAB.runTests(bot, MagicBooleans.trace_mode);     }
        else if (action.equals("shadow")) { MagicBooleans.trace_mode = false; bot.shadowChecker();}
        else if (action.equals("iqtest")) { ChatTest ct = new ChatTest(bot);
                try {
                    ct.testMultisentenceRespond();
                }
            catch (Exception ex) { ex.printStackTrace(); }
            }
        else System.out.println("Unrecognized action "+action);
    }
    public static void convert(Bot bot, String action) {
        if (action.equals("aiml2csv")) {
        	bot.writeAIMLIFFiles();
        	/*if (MagicBooleans.trace_mode) System.out.println("writeAIMLIFFiles");
            HashMap<String, BufferedWriter> fileMap = new HashMap<String, BufferedWriter>();
            Category b = new Category(0, "BRAIN BUILD", "*", "*", new Date().toString(), "update.aiml");
            Graphmaster brain=bot.brain;
            brain.addCategory(b);
            ArrayList<Category> brainCategories = brain.getCategories();
            Collections.sort(brainCategories, Category.CATEGORY_NUMBER_COMPARATOR);
            String aimlif_path=bot.aimlif_path;
            for (Category c : brainCategories) {
                try {
                    BufferedWriter bw;
                    String fileName = c.getFilename();
                    if (fileMap.containsKey(fileName)) bw = fileMap.get(fileName);
                    else {
                    	String fn = aimlif_path+"/"+fileName+MagicStrings.aimlif_file_suffix;
                    	bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn), StandardCharsets.UTF_8));
                        fileMap.put(fileName, bw);

                    }
                    bw.write(Category.categoryToIF(c));
                    bw.newLine();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            Set<String> set = fileMap.keySet();
            for (Object aSet : set) {
                BufferedWriter bw = fileMap.get(aSet);
                //Close the bw
                try {
                    if (bw != null) {
                        bw.flush();
                        bw.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();

                }

            }
            File dir = new File(aimlif_path);
            dir.setLastModified(new Date().getTime());*/
    }else if (action.equals("csv2aiml")){
        	bot.writeAIMLFiles();
        	/*if (MagicBooleans.trace_mode) System.out.println("writeAIMLFiles");
            HashMap<String, BufferedWriter> fileMap = new HashMap<String, BufferedWriter>();
            Category b = new Category(0, "BRAIN BUILD", "*", "*", new Date().toString(), "update.aiml");
            Graphmaster brain=bot.brain;
            brain.addCategory(b);
            //b = new Category(0, "PROGRAM VERSION", "*", "*", MagicStrings.program_name_version, "update.aiml");
            //brain.addCategory(b);
            ArrayList<Category> brainCategories = brain.getCategories();
            Collections.sort(brainCategories, Category.CATEGORY_NUMBER_COMPARATOR);
            for (Category c : brainCategories) {
                if (!c.getFilename().equals(MagicStrings.null_aiml_file))
                try {
                    //System.out.println("Writing "+c.getCategoryNumber()+" "+c.inputThatTopic());
                    BufferedWriter bw;
                    String fileName = c.getFilename();
                    if (fileMap.containsKey(fileName)) bw = fileMap.get(fileName);
                    else {
                        String copyright = Utilities.getCopyright(bot, fileName);
                        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
                        fileMap.put(fileName, bw);
                        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" +"<aiml>\n");
                        bw.write(copyright);
                        //bw.newLine();
                    }
                    bw.write(Category.categoryToAIML(c)+"\n");
                    //bw.newLine();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            Set<String> set = fileMap.keySet();
            for (Object aSet : set) {
                BufferedWriter bw = fileMap.get(aSet);
                //Close the bw
                try {
                    if (bw != null) {
                        bw.write("</aiml>\n");
                        bw.flush();
                        bw.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();

                }

            }
            String aiml_path=bot.aiml_path;
            File dir = new File(aiml_path);
            dir.setLastModified(new Date().getTime());*/
        }
    }
    public static void getGloss (Bot bot, String filename) {
        System.out.println("getGloss");
        try{
            // Open the file that is the first
            // command line parameter
            File file = new File(filename);
            if (file.exists()) {
                FileInputStream fstream = new FileInputStream(filename);
                // Get the object
                getGlossFromInputStream(bot, fstream);
                fstream.close();
            }
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void getGlossFromInputStream (Bot bot, InputStream in)  {
        System.out.println("getGlossFromInputStream");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int cnt = 0;
        int filecnt = 0;
        HashMap<String, String> def = new HashMap<String, String>();
        try {
            //Read File Line By Line
            String word; String gloss;
            word = null;
            gloss = null;
            while ((strLine = br.readLine()) != null)   {

                if (strLine.contains("<entry word")) {
                    int start = strLine.indexOf("<entry word=\"")+"<entry word=\"".length();
                    int end = strLine.indexOf("#");

                    word = strLine.substring(start, end);
                    word = word.replaceAll("_"," ");
                    System.out.println(word);

                }
                else  if (strLine.contains("<gloss>")) {
                    gloss = strLine.replaceAll("<gloss>","");
                    gloss = gloss.replaceAll("</gloss>","");
                    gloss = gloss.trim();
                    System.out.println(gloss);

                }


                if (word != null && gloss != null) {
                    word = word.toLowerCase().trim();
                    if (gloss.length() > 2) gloss = gloss.substring(0, 1).toUpperCase()+gloss.substring(1, gloss.length());
                    String definition;
                    if (def.keySet().contains(word))  {
                        definition = def.get(word);
                        definition = definition+"; "+gloss;
                    }
                    else definition = gloss;
                    def.put(word, definition);
                    word = null;
                    gloss = null;
                }
            }
            Category d = new Category(0,"WNDEF *","*","*","unknown","wndefs"+filecnt+".aiml");
            bot.brain.addCategory(d);
            for (String x : def.keySet()) {
                word = x;
                gloss = def.get(word)+".";
                cnt++;
                if (cnt%5000==0) filecnt++;

                Category c = new Category(0,"WNDEF "+word,"*","*",gloss,"wndefs"+filecnt+".aiml");
                System.out.println(cnt+" "+filecnt+" "+c.inputThatTopic()+":"+c.getTemplate()+":"+c.getFilename());
                Nodemapper node;
                if ((node = bot.brain.findNode(c)) != null) node.category.setTemplate(node.category.getTemplate()+","+gloss);
                bot.brain.addCategory(c);


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
