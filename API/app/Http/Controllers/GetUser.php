<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\UserKL;

class GetUser extends Controller
{
    public function GetUser($user){
        $row = UserKL::all()->where('name', $user);
    return response($row->name . " " . $row->HS);
}
}
