<?php

namespace App\Http\Controllers;

use App\UserKL;
use Illuminate\Http\Request;

class AddUser extends Controller
{
    public function AddUser($user, $hs){
        if (UserKL::all()->get('name', $user)->exists()){
            UserKL::where('name', $user)->update(['HS' => $hs]);
        }
        else
            UserKL::create(['name'=>$user, 'HS' => $hs]);
    }
}
