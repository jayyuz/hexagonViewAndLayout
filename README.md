## HexagonView  
This project demonstrate how to custom a recyclerview layout manager. In this example, i create a custome view named HexagonView. Bucause we create a not normal shape view, we should take over the touch and click event calculation. That is easy, i override the ***onTouchEvent*** method, and calculate touch is in the view's boundary by using ***android.graphics.Region#contains***. If it's out of the ragion, just return false in ***onTouchEvent*** mathod. And then, the framework will dispatch the touch event to other views.

## About The LayoutManager  
It's obvious that, the layout manager manage the recyclerview's child's layout. We should focus on the ***onLayoutChildren*** method. In this method, we finish calculate our every child view's position and laid them out. 

![demo](./img/layoutmanager.gif)