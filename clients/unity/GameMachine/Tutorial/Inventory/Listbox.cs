using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Listbox
{
    private Vector2 scrollViewVector = Vector2.zero;
    public Rect dropDownRect = new Rect (125, 50, 125, 300);
    private Dictionary<string, string> items = new Dictionary<string, string> ();

    public string selection;
    public bool selected = false;
    public bool show = false;
    public float itemWidth;

    public Listbox (int left, int top, int width, int height)
    {
        dropDownRect = new Rect (left, top, width, height);
        itemWidth = dropDownRect.height;

    }

    public bool HasItems ()
    {
        return items.Count > 0;
    }

    public void ClearItems ()
    {
        items.Clear ();
        selection = null;
        selected = false;
    }

    public void RemoveItem (string id)
    {
        items.Remove (id);
        if (selection == id) {
            selection = null;
            selected = false;
        }
    }

    public void AddItem (string id, string name)
    {
        items [id] = name;
    }

    public void Show (string selectDefault)
    {  
        if (!HasItems ()) {
            return;
        }

        if (GUI.Button (new Rect (dropDownRect.x - 100, dropDownRect.y, dropDownRect.width, 25), "")) {
            if (!show) {
                show = true;
            } else {
                show = false;
            }
        }
        
        if (show) {
            scrollViewVector = GUI.BeginScrollView (
                new Rect (dropDownRect.x - 100, dropDownRect.y + 25, dropDownRect.width, dropDownRect.height),
                scrollViewVector,
                new Rect (0, 0, dropDownRect.width, Mathf.Max (dropDownRect.height, items.Count * 25))
            );
            
            GUI.Box (new Rect (0, 0, dropDownRect.width, Mathf.Max (dropDownRect.height, items.Count * 25)), "");

            int index = 0;
            foreach (string id in items.Keys) {
                string name = items [id];
                if (GUI.Button (new Rect (0, index * 25, itemWidth, 25), "")) {
                    show = false;
                    selection = id;
                    selected = true;
                }
                
                GUI.Label (new Rect (5, index * 25, itemWidth, 25), name);
                index++;
            }

            GUI.EndScrollView ();   
        }
        string item;
        if (selected) {
            item = items [selection];
        } else {
            item = selectDefault;
        }
        GUI.Label (new Rect (dropDownRect.x - 95, dropDownRect.y, dropDownRect.height, 25), item);
        
    }
}