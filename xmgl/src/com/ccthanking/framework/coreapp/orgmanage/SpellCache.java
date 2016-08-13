package com.ccthanking.framework.coreapp.orgmanage;

import java.util.*;

import java.sql.*;

import com.ccthanking.framework.common.DBUtil;

public class SpellCache
{
    private static SpellCache spellcache;
    ArrayList spellList = null;
    int icount = 0;

    private SpellCache()
    {
        try
        {
            if (spellcache == null)
                init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            spellcache = null;
        }
    }

    public static synchronized SpellCache getInstance()
    {
        if (spellcache == null)
        {
            spellcache = new SpellCache();
        }
        return spellcache;
    }

    private void init()
        throws Exception
    {
        Connection conn = null;
        Statement stmt = null;
        try
        {
            conn = DBUtil.getConnection();
            String sql =
                "select t.word,t.spell,t.aspell from fs_spell t order by t.word";
            stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            spellList = new ArrayList();
            while (rst.next())
            {
                char word = rst.getString("word").charAt(0);
                char spell = rst.getString("spell").charAt(0);
                String aspell = rst.getString("aspell");
                spellList.add(new Spell(word, spell, aspell));
                icount++;
            }
            rst.close();
            stmt.close();
            rst = null;
            stmt = null;
        }
        catch (Exception e)
        {
          System.out.println("失败！");
            throw e;
        }
        finally
        {
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
            stmt = null;
            conn = null;
        }
    }

    public int getCount()
    {
        return this.icount;
    }

    public Spell getObject(char word)
    {
        Iterator it = this.spellList.iterator();
        while (it.hasNext())
        {
            Spell spell = (Spell) it.next();
            if (spell.equals(word))
                return spell;
        }
        return null;
    }

    public String getSpell(char word)
    {
        Spell spell = getObject(word);
        if (spell == null)
            return "";
        return String.valueOf(spell.getSpell());
    }

    public String getAspell(char word)
    {
        Spell spell = getObject(word);
        if (spell == null)
            return "";
        return String.valueOf(spell.getAspell());
    }

    public String getSpell(String word)
    {
        if (word == null)
            return null;
        if (word.equals(""))
            return null;
        char[] ch = word.toCharArray();
        String spell = "";
        for (int i = 0; i < ch.length; i++)
        {
            spell += getSpell(ch[i]);
        }
        return spell;
    }

    public String getAspell(String word)
    {
        if (word == null)
            return null;
        if (word.equals(""))
            return null;
        char[] ch = word.toCharArray();
        String spell = "";
        for (int i = 0; i < ch.length; i++)
        {
            spell += getAspell(ch[i]);
        }
        return spell;
    }
}

class Spell
{
    private char word;
    private char spell;
    private String aspell;

    public Spell()
    {}

    public Spell(char word, char spell, String aspell)
    {
        this.word = word;
        this.spell = spell;
        this.aspell = aspell;
    }

    public String getAspell()
    {
        return aspell;
    }

    public void setAspell(String aspell)
    {
        this.aspell = aspell;
    }

    public char getSpell()
    {
        return spell;
    }

    public void setSpell(char spell)
    {
        this.spell = spell;
    }

    public char getWord()
    {
        return word;
    }

    public void setWord(char word)
    {
        this.word = word;
    }

    public boolean equals(char o)
    {
        if (this.word == o)
            return true;
        return false;
    }

}