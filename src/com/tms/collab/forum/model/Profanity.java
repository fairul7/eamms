package com.tms.collab.forum.model;

import kacang.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Profanity {

    private static Profanity profanity = null;

    private static final String FILE_NAME = "Profanity.txt";
    private static final String FILTER_REPLACE = "*CENSORED*";
    public static final String STRING_SEPERATOR = "|~|";
    private List words;
    private String path = "";
    private Log logger = Log.getLog(getClass());

    private Profanity() {
        Init();
    }

    public synchronized void Init() {
        InputStream in = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        words = new ArrayList();

        if (in != null) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(in));
                String word;
                while ((word = br.readLine()) != null && (word = word.trim()).length() > 0) {
                    words.add(word);
                }
                Collections.sort(words);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                try {
                    if (br != null) br.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

        }

    }

    public static synchronized Profanity getInstance() {
        if (profanity == null) {
            profanity = new Profanity();
        }

        return profanity;
    }

    public List getWords() {
        return words;
    }

    /**
     * @param Accept any string
     * @return String with profanity replaced according to the list
     */
    public String filter(String contents) {
        StringBuffer filtered = null;
        String temp = contents.toLowerCase();
        String word;
        int index1;

        for (int i = words.size() - 1; i >= 0; i--) {
            word = (String) words.get(i);
            while ((index1 = temp.lastIndexOf(" " + word.toLowerCase() + " ")) > -1) {
                index1++;
                filtered = new StringBuffer();
                filtered.append(contents.substring(0, index1));
                filtered.append(FILTER_REPLACE);
                filtered.append(contents.substring(index1 + word.length()));
                contents = filtered.toString();
                temp = contents.toLowerCase();
            }
        }

        for (int i = words.size() - 1; i >= 0; i--) {
            word = (String) words.get(i);
            if ((index1 = temp.lastIndexOf(" " + word.toLowerCase())) > -1) {
                index1++;
                filtered = new StringBuffer();
                filtered.append(contents.substring(0, index1));
                filtered.append(FILTER_REPLACE);
                filtered.append(contents.substring(index1 + word.length()));
                contents = filtered.toString();
                temp = contents.toLowerCase();
            }

            if ((index1 = temp.indexOf(word.toLowerCase() + " ")) > -1) {
                filtered = new StringBuffer();
                filtered.append(contents.substring(0, index1));
                filtered.append(FILTER_REPLACE);
                filtered.append(contents.substring(index1 + word.length()));
                contents = filtered.toString();
                temp = contents.toLowerCase();
            }
        }

        logger.debug("~~~~~~~~~~~~~~~~~content = " + contents);
        return contents;
    }

/*
    public boolean add(String profanities)
    {
        return add(profanities, STRING_SEPERATOR);
    }

    public boolean add(String profanities, String seperator)
    {
        List profanityList = new ArrayList();

        if(seperator == null || seperator.trim().equals(""))
            seperator = STRING_SEPERATOR;
        try
        {
            StringTokenizer stzTokenizer = new StringTokenizer(profanities, seperator);
            while(stzTokenizer.hasMoreElements())
            {
                profanityList.add(stzTokenizer.nextElement());
            }
            return add(profanityList);
        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

	public boolean add(List profanities)
	{
		String profanity;
		boolean add = true;
		try
		{

            InputStream in = getClass().getClassLoader().getResourceAsStream(FILE_NAME);

			File file = new File(path);
			PrintWriter out = null;

			if(profanities.size() > 0)
			{
				if (!file.exists())
				{
					file.mkdirs();
				}
				file = new File(file, FILE_NAME);
				out = new PrintWriter(new FileWriter(file.getAbsolutePath(), true));
			}

			for(int i=0; i < profanities.size(); i++)
			{
				profanity = (String)profanities.get(i);

				if((Collections.binarySearch(words, profanity)) < 0)
				{
					try
					{
						out.println(profanity);
					}
					catch(Exception e)
					{
						logger.error(e.getMessage(), e);
						return false;
					}
				}
				else
					add  = false;
			}
			if (out != null)
			   out.close();
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			return false;
		}
		return add;
	}


	public boolean remove(String profanities)
	{
		String profanity;
		boolean remove = true;

		try
		{
            StringTokenizer stzTokenizer = new StringTokenizer(profanities, STRING_SEPERATOR);

			if(stzTokenizer.countTokens() > 0)
			{
				File file = new File(path + (!path.equals("")?File.separator:"") + FILE_NAME);

				if (file.exists())
				{
					PrintWriter out = new PrintWriter(new FileWriter(file.getAbsolutePath()));
					while(stzTokenizer.hasMoreElements())
					{
						profanity = (String)stzTokenizer.nextElement();

						if(Collections.binarySearch(words, profanity) > -1)
						{
							try
							{
								words.remove(profanity);
							}
							catch(Exception e)
							{
								logger.error(e.getMessage(), e);
								return false;
							}
						}
						else
							remove = false;
					}
					for(int j=0; j < words.size(); j++)
					{
						out.println(words.get(j));
					}

					if(out != null)
						out.close();
				}
				else
					remove = false;
			}
			else
				remove = false;
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			return false;
		}
		return remove;
	}
*/

}