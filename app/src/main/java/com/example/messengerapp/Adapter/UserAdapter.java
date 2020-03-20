package com.example.messengerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messengerapp.MessageActivity;
import com.example.messengerapp.Model.Chat;
import com.example.messengerapp.Model.Users;
import com.example.messengerapp.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Users> usersList;
    private boolean isChat;

    private String theLastMsg;

    public UserAdapter(Context context, ArrayList<Users> users, boolean isChat) {
        this.context = context;
        this.usersList = users;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users=usersList.get(position);
        holder.username.setText(users.getUsername());
        if (users.getImageURL().equals("default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(users.getImageURL()).into(holder.profileImage);
        }
        if (isChat){
            lastMessage(users.getId(),holder.lastMsg);
        }else {
            holder.lastMsg.setVisibility(View.GONE);
        }

        if (isChat){
            if (users.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, MessageActivity.class);
                intent.putExtra("userid",users.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialTextView username,lastMsg;
        public ImageView profileImage,img_on,img_off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.username);
            profileImage=itemView.findViewById(R.id.profile_image);
            img_on=itemView.findViewById(R.id.img_on);
            img_off=itemView.findViewById(R.id.img_off);
            lastMsg=itemView.findViewById(R.id.last_msg);

        }
    }
    //check for last message
    private void lastMessage(final String userid, final MaterialTextView lastMsg){
        theLastMsg= "default";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat= snapshot.getValue(Chat.class);
                    if (chat.getReciever().equals(firebaseUser.getUid()) &&
                    chat.getSender().equals(userid)||
                            chat.getReciever().equals(userid) &&
                                    chat.getSender().equals(firebaseUser.getUid())){
                        theLastMsg=chat.getMessage();
                    }
                }
                switch (theLastMsg){
                    case "default":
                        lastMsg.setText("No Message");
                        break;

                    default:
                        lastMsg.setText(theLastMsg);
                        break;
                }
                theLastMsg = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
